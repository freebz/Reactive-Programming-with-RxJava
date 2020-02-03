// rx.Observable 해부하기

class NaturalNumbersIterator implements Iterator<BigInteger> {

    private BigInteger current = BigInteger.ZERO;

    public boolean hasNext() {
	return true;
    }

    @Override
    public BigInteger next() {
	current = current.add(BigInteger.ONE);
	return current;
    }
}
