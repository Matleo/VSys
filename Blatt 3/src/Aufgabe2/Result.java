package Aufgabe2;

import java.io.Serializable;

public class Result implements Serializable{
    private static final long serialVersionUID = 34745823454363L;
    private boolean isPrime;
    private long p;
    private long w;

    public long getW() {
        return w;
    }

    public void setW(long w) {
        this.w = w;
    }

    public Result(boolean isPrime, long p, long w){
        this.isPrime = isPrime;
        this.p = p;
        this.w = w;
    }

    public boolean isPrime() {
        return isPrime;
    }

    public void setPrime(boolean prime) {
        isPrime = prime;
    }

    public long getP() {
        return p;
    }

    public void setP(long p) {
        this.p = p;
    }
}
