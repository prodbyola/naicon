use converter::ConversionOption;

mod converter;
mod probe;

#[tokio::main]
async fn main() -> std::io::Result<()> {
    let input = "demo.mkv";
    let output = "demo.mp4";

    let opts = ConversionOption {
        input,
        output,
        ..Default::default()
    };
    
    converter::run(opts).await?;
    Ok(())
}
