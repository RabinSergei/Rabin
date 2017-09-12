package rabin;

public class Match {
    String fileName;
    int lineNo;
    String line;

    public Match(String fileName, int lineNo, String line) {
        this.fileName = fileName;
        this.lineNo = lineNo;
        this.line = line;
    }

    @Override
    public String toString() {
        return "Occurence in " + fileName + "\n" +
                lineNo + ": " + line + "\n";
    }
}
