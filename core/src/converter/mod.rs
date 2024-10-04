use params::{AudioCodec, OutputFormat};
use tokio::{
    io::{AsyncBufReadExt, BufReader},
    process::Command,
};

use crate::probe::Probe;
use self::params::VideoCodec;

pub(self) mod params;


#[derive(Default)]
pub struct ConversionOption<'a> {
    pub input: &'a str,
    pub output: &'a str,
    pub audio_bitrate: Option<&'a str>,
    pub audio_code: Option<AudioCodec>,
    pub video_code: Option<VideoCodec>,
    pub video_bitrate: Option<&'a str>,
    pub output_format: Option<OutputFormat>,
}

impl<'a> IntoIterator for &'a ConversionOption<'a> {
    type Item = &'a str;
    type IntoIter = std::vec::IntoIter<&'a str>;

    fn into_iter(self) -> Self::IntoIter {
        let mut opts = vec!["-i", &self.input, "-progress", "pipe:1"];

        // video codec 
        if let Some(vc) = &self.video_code {
            opts.push("-c:v");
            opts.push(vc.as_ref());
        }

        // video bitrate
        if let Some(br) = self.video_bitrate  {
            opts.push("-b:v");
            opts.push(br);
        }
        
        // set audio codec
        if let Some(ac) = &self.audio_code {
            opts.push("-c:a");
            opts.push(ac.as_ref());
        }

        // audio bitrate
        if let Some(br) = self.audio_bitrate  {
            opts.push("-a:v");
            opts.push(br);
        }

        // output format
        if let Some(f) = &self.output_format {
            opts.push("-f");
            opts.push(f.as_ref());
        }

        opts.push(&self.output);

        opts.into_iter()
    }
}

/// Run conversion of an input file to an output file
pub async fn run<'a>(opts: ConversionOption<'a>) -> std::io::Result<()> {
    let mut cmd = Command::new("ffmpeg")
        .args(opts.into_iter())
        .stdout(std::process::Stdio::piped())
        .stderr(std::process::Stdio::piped())
        .spawn()?;

    let out = cmd.stdout.take().unwrap();
    let err = cmd.stderr.take().unwrap();

    let out_reader = BufReader::new(out);
    let err_reader = BufReader::new(err);

    let mut out_lines = out_reader.lines();
    let mut err_lines = err_reader.lines();

    let probe = Probe::new(&opts.input)?;

    tokio::spawn(async move {
        while let Some(line) = out_lines.next_line().await.unwrap() {
            if line.starts_with("out_time_us") {
                let split: Vec<&str> = line.split("=").collect();

                let ds = split.get(1).unwrap_or(&"0"); // duration str
                let pd = ds.parse::<f64>().unwrap(); // parse duration

                let current_duration = pd / 1e6;
                let total_duration = probe.duration;
                let percent = ((current_duration * 100.0) / total_duration).round();
                println!(
                    "total: {total_duration}, current: {current_duration}, percent: {percent}"
                );
            }
        }
    })
    .await?;

    tokio::spawn(async move {
        while let Some(line) = err_lines.next_line().await.unwrap() {
            eprintln!("stderr: {}", line);
        }
    })
    .await?;

    Ok(())
}

#[cfg(test)]
mod test {
    use crate::converter::{self, ConversionOption};

    #[tokio::test]
    async fn test_converter() {
        let input = "demo.mkv";
        let output = "demo.mp4";

        let opts = ConversionOption {
            input,
            output,
            ..Default::default()
        };

        let con = converter::run(opts).await;
        if let Err(err) = &con {
            eprintln!("err: {err}")
        }

        assert!(con.is_ok())
    }
}
