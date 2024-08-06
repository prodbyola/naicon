use std::{io::{BufRead, BufReader, Result}, process::Command};

async fn convert(input: &str, output: &str) -> std::io::Result<()> {
    let mut cmd = Command::new("ffmmpeg")
        .args(["-i", input, output])
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
    });

    tokio::spawn(async move {
        while let Some(line) = err_lines.next() {
            if let Ok(line) = line {
                eprintln!("stderr: {}", line);
            }
        }
    });

    Ok(())
}

#[tokio::main]
async fn main() -> std::io::Result<()> {
    convert("demo.mkv", "demo.mp4").await?;
    Ok(())
}
