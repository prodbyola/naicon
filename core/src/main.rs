use std::{io::{BufRead, BufReader, Error}, process::Command};

pub(crate) fn probe(input: &str) -> Result<(), Error> {
    let cmd = Command::new("ffprobe")
        .args(["-select_streams", "v:0", input])
        .output()?;

    let out = String::from_utf8(cmd.stdout).unwrap();
    let err = String::from_utf8(cmd.stderr).unwrap();

    println!("out: {out}, err: {err}");

    Ok(())
}

pub(crate) async fn convert(input: &str, output: &str) -> std::io::Result<()> {
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

    tokio::spawn(async move {
        while let Some(line) = out_lines.next() {
            if let Ok(line) = line {
                println!("stdout: {}", line);
            }
        }
    }).await?;

    tokio::spawn(async move {
        while let Some(line) = err_lines.next() {
            if let Ok(line) = line {
                eprintln!("stderr: {}", line);
            }
        }
    }).await?;

    Ok(())
}

#[tokio::main]
async fn main() -> std::io::Result<()> {
    convert("demo.mkv", "demo.mp4").await?;
    Ok(())
}

#[cfg(test)]
mod test {
    use crate::{convert, probe};

    #[tokio::test]
    async fn test_conversion() {
        let input = "demo.mkv";
        let output = "demo.mp4";

        let con = convert(input, output).await;
        if let Err(err) = &con {
            eprintln!("err: {err}")
        }

        assert!(con.is_ok())
    }

    #[test]
    fn test_probe() {
        let pb = probe("demo.mkv");
        assert!(pb.is_ok());
    }
}