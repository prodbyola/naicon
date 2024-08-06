use tokio::{
    io::{AsyncBufReadExt, BufReader},
    process::Command,
};

use crate::probe::Probe;

pub async fn run(input: &str, output: &str) -> std::io::Result<()> {
    let mut cmd = Command::new("ffmpeg")
        .args(["-i", input, "-progress", "pipe:1", output])
        .stdout(std::process::Stdio::piped())
        .stderr(std::process::Stdio::piped())
        .spawn()?;

    let out = cmd.stdout.take().unwrap();
    let err = cmd.stderr.take().unwrap();

    let out_reader = BufReader::new(out);
    let err_reader = BufReader::new(err);

    let mut out_lines = out_reader.lines();
    let mut err_lines = err_reader.lines();

    let probe = Probe::new(input)?;

    tokio::spawn(async move {
        while let Some(line) = out_lines.next_line().await.unwrap() {
            if line.starts_with("out_time_us") {
                let split: Vec<&str> = line.split("=").collect();

                let ds = split.get(1).unwrap_or(&"0");
                let ps = ds.parse::<f64>().unwrap();

                let current_duration = ps / 1e6;
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
    use crate::converter;

    #[tokio::test]
    async fn test_converter() {
        let input = "demo.mkv";
        let output = "demo.mp4";

        let con = converter::run(input, output).await;
        if let Err(err) = &con {
            eprintln!("err: {err}")
        }

        assert!(con.is_ok())
    }
}
