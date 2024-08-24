use tokio::{
    io::{AsyncBufReadExt, BufReader},
    process::Command,
};

use crate::probe::Probe;

pub struct ConversionOption {
    pub input: String,
    pub output: String,
}

impl<'a> IntoIterator for &'a ConversionOption {
    type Item = &'a str;

    type IntoIter = std::array::IntoIter<&'a str, 5>;

    fn into_iter(self) -> Self::IntoIter {
        ["-i", &self.input, "-progress", "pipe:1", &self.output].into_iter()
    }
}

/// Run conversion of an input file to an output file
pub async fn run(opts: ConversionOption) -> std::io::Result<()> {
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
                println!("total: {total_duration}, current: {current_duration}, percent: {percent}");
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
        let input = "demo.mkv".to_string();
        let output = "demo.mp4".to_string();

        let opts = ConversionOption { input, output };

        let con = converter::run(opts).await;
        if let Err(err) = &con {
            eprintln!("err: {err}")
        }

        assert!(con.is_ok())
    }
}
