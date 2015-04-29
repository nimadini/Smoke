package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatementCoverage {
    private static StatementCoverage sc = new StatementCoverage();

    private StatementCoverage() {
        tcToBlkSet = new HashMap<TestCase, Set<Block>>();
    }

    public static StatementCoverage getStatementCoverage() {
        return sc;
    }

    private final Set<String> elems = new HashSet<String>();

    private Map<TestCase, Set<Block>> tcToBlkSet;

    public void addElem(String elem) {
        elems.add(elem);
    }

    public Set<String> getElems() {
        return elems;
    }

    public void addTestCase(String completeName) {
        if (tcToBlkSet.containsKey(new TestCase(completeName))) {
            throw new RuntimeException(); // TODO
        }

        else {
            tcToBlkSet.put(new TestCase(completeName), new HashSet<Block>());
        }
    }
}
