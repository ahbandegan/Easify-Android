package ir.amirhesambandegan.easify_file

/**
 * Represents the types of media that can be picked using the file pickers.
 */
enum class MediaType {
    /** Only images are allowed. */
    ImageOnly,
    /** Only videos are allowed. */
    VideoOnly,
    /** Both images and videos are allowed. */
    ImageAndVideo
}