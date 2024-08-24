use converter::ConversionOption;

mod converter;
mod probe;

#[tokio::main]
async fn main() -> std::io::Result<()> {
    let input = "demo.mkv".to_string();
    let output = "demo.mp4".to_string();

    let opts = ConversionOption { input, output };
    converter::run(opts).await?;
    Ok(())
}
