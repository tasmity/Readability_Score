package readability;

interface IndexRead {
    default double checkARI(int characters, int words, int sentences) {
        return (4.71 * ((double) characters / (double) words) + 0.5 * ((double) words / (double) sentences) - 21.43);
    }

    default double checkFK(int words, int sentences, int syllables) {
        return (0.39 * ((double) words / (double) sentences) + 11.8 * ((double) syllables / (double) words) - 15.59);
    }

    default double checkSMOG(int polysyllables, int sentences) {
        return (1.043 * (Math.sqrt((double) polysyllables * (30 / (double) sentences)) + 3.1291));
    }

    default double checkCL(int words, int characters, int sentences) {
        return (0.0588 * ((double) characters / (double) words * 100) - 0.296 *
                ((double) sentences / (double) words * 100) - 15.8);
    }

    default int checkAge(int score) {
        return switch (score) {
            case 1, 2 -> score + 5;
            case 3 -> 9;
            case 4, 5, 6, 7, 8, 9, 10, 11, 12 -> score + 6;
            case 13 -> 24;
            default -> 25;
        };
    }

    default double checkAverageIndex(int characters, int words, int sentences, int syllables, int polysyllables) {
        return (((double) checkAge((int) Math.round(checkARI(characters, words, sentences))) +
                (double) checkAge((int) Math.round(checkFK(words, sentences, syllables))) +
                (double) checkAge((int) Math.round(checkSMOG(polysyllables, sentences))) +
                (double) checkAge((int) Math.round(checkCL(words, characters, sentences)))) / 4);
    }
}