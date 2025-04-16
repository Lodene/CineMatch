export class RandomUtil {
    /**
     * Return a random number in [min; max[
     * @param min lower treshold (inclusive)
     * @param max upper treshold (excusive) 
     * @returns 
     */
    static getRandomNumberFromRange(min: number, max: number): number {
        const minCeiled = Math.ceil(min);
        const maxFloored = Math.floor(max);
        return Math.floor(Math.random() * (maxFloored - minCeiled) + minCeiled);
    }
}