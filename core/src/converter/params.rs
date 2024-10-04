pub enum VideoCodec {
    H264,
    H265,
    MPEG4,
}

impl AsRef<str> for VideoCodec {
    fn as_ref(&self) -> &str {
        match self {
            VideoCodec::H264 => "libx264",
            VideoCodec::H265 => "libx265",
            VideoCodec::MPEG4 => "mpeg4",
        }
    }
}

pub enum AudioCodec {
    AAC,
    MP3,
    Vorbis
}

impl AsRef<str> for AudioCodec {
    fn as_ref(&self) -> &str {
        match self {
            AudioCodec::AAC => "aac",
            AudioCodec::MP3 => "mp4",
            AudioCodec::Vorbis => "libvorbis",
        }
    }
}

pub enum OutputFormat {
    MP4,
    MKV,
    MOV
}

impl AsRef<str> for OutputFormat {
    fn as_ref(&self) -> &str {
        match self {
            OutputFormat::MP4 => "mp4",
            OutputFormat::MKV => "mkv",
            OutputFormat::MOV => "mov",
        }
    }
}

pub struct VideoScale<'a> {
    width: &'a str,
    height: &'a str
}