
mod converter;
mod probe;

#[tokio::main]
async fn main() -> std::io::Result<()> {
    converter::run("demo.mkv", "demo.mp4").await?;
    Ok(())
}
