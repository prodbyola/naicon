use std::process::Command;

fn convert(input: &str, output: &str) {
    let mut cmd = Command::new("ffmmpeg");

    let out = cmd
        .args(["-i", input, output])
        .output()
        .expect("Unable to execute");
    if out.status.success() {
        println!("conversion completed")
    } else {
        eprintln!("conversion error: {:?}", out.stderr)
    }
}

fn main() {
    convert("demo.mkv", "demo.mp4");
}
