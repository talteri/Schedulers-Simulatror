package Schedulers;

class Process {
    int index;
    int arrivalTime;
    int length;

    int remaining;

    public Process(int index,int arrivalTime, int length) {
        this.index = index;
        this.arrivalTime = arrivalTime;
        this.length = length;
        this.remaining = length;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getLength() {
        return length;
    }

    public int getIndex() {
        return index;
    }

    public void updateLength(int length) {
        this.length -= length;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public int getRemaining() {
        return remaining;
    }
}