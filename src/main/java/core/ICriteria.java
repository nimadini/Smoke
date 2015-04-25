package core;

public interface ICriteria {
    public CriteriaMatrix createMatrix(String className) throws ClassNotFoundException;
}