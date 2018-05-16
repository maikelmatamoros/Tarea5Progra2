package domain;

// Buffer interface specifies methods called by Producer and Consumer.
public interface Buffer
{

    public void set();  // place value into Buffer

    public void get();              // return value from Buffer
}
