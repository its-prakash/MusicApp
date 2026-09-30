CREATE TABLE IF NOT EXISTS songs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    artist VARCHAR(255),
    album VARCHAR(255),
    genre VARCHAR(255),
    audio_url VARCHAR(255),
    cover_image_url VARCHAR(255),
    duration INT
);
