/**
 * Class used to format image Utils
 */
export class MovieImageUtils {
    static constructUrl(url: string): string {
        return !!url ? basicUrl + url : '';
    }
}

const basicUrl: string = "https://image.tmdb.org/t/p/w500";