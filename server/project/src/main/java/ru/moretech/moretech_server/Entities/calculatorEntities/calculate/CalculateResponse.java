package ru.moretech.moretech_server.Entities.calculatorEntities.calculate;

public class CalculateResponse {
    private ProgramResponse program;
    private Ranges ranges;
    private Result result;

    public ProgramResponse getProgram() {
        return program;
    }

    public void setProgram(ProgramResponse program) {
        this.program = program;
    }

    public Ranges getRanges() {
        return ranges;
    }

    public void setRanges(Ranges ranges) {
        this.ranges = ranges;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
