package core;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

public class StatementCoverage implements ICriteria {
    public CriteriaMatrix createMatrix(String className) throws ClassNotFoundException {
        JavaClass jc = Repository.lookupClass(className);
        ClassGen cg = new ClassGen(jc);
        ConstantPoolGen cpg = cg.getConstantPool();

        for (Method method : cg.getMethods()) {
            MethodGen mg = new MethodGen(method, cg.getClassName(), cpg);
            InstructionList il = mg.getInstructionList();
            System.out.println(method.getName() + "::" + il.size());
        }

        return null;
    }
}
