package top.mrys.vertx.common.utils;


import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import lombok.SneakyThrows;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.springframework.core.DefaultParameterNameDiscoverer;
import top.mrys.vertx.common.config.ConfigRepo;


/**
 * @author mrys
 * @date 2020/9/22
 */
public class ASMUtil {

  public static String[] getMethodParamNames(final Method method) throws IOException {

    final String methodName = method.getName();
    final Class<?>[] methodParameterTypes = method.getParameterTypes();
    final int methodParameterCount = methodParameterTypes.length;
    final String className = method.getDeclaringClass().getName();
    final boolean isStatic = Modifier.isStatic(method.getModifiers());
    final String[] methodParametersNames = new String[methodParameterCount];

    ClassReader cr = new ClassReader(className);
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    cr.accept(new ClassVisitor(Opcodes.ASM9,cw) {
      public MethodVisitor visitMethod(int access, String name, String desc, String signature,
          String[] exceptions) {

        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        final Type[] argTypes = Type.getArgumentTypes(desc);

        //参数类型不一致
        if (!methodName.equals(name) || !matchTypes(argTypes, methodParameterTypes)) {
          return mv;
        }
        return new MethodVisitor(Opcodes.ASM9,mv) {
          public void visitLocalVariable(String name, String desc, String signature, Label start,
              Label end, int index) {
            //如果是静态方法，第一个参数就是方法参数，非静态方法，则第一个参数是 this ,然后才是方法的参数
            int methodParameterIndex = isStatic ? index : index - 1;
            if (0 <= methodParameterIndex && methodParameterIndex < methodParameterCount) {
              methodParametersNames[methodParameterIndex] = name;
            }
            super.visitLocalVariable(name, desc, signature, start, end, index);
          }
        };
      }
    }, 0);
    return methodParametersNames;
  }

  /**
   * 比较参数是否一致
   */
  private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
    if (types.length != parameterTypes.length) {
      return false;
    }
    for (int i = 0; i < types.length; i++) {
      if (!Type.getType(parameterTypes[i]).equals(types[i])) {
        return false;
      }
    }
    return true;
  }
/**
 * ACC_ABSTRACT	1024
 * ACC_ANNOTATION	8192
 * ACC_BRIDGE	64
 * ACC_DEPRECATED	131072
 * ACC_ENUM	16384
 * ACC_FINAL	16
 * ACC_INTERFACE	512
 * ACC_MANDATED	32768
 * ACC_MODULE	32768
 * ACC_NATIVE	256
 * ACC_OPEN	32
 * ACC_PRIVATE	2
 * ACC_PROTECTED	4
 * ACC_PUBLIC	1
 * ACC_STATIC	8
 * ACC_STATIC_PHASE	64
 * ACC_STRICT	2048
 * ACC_SUPER	32
 * ACC_SYNCHRONIZED	32
 * ACC_SYNTHETIC	4096
 * ACC_TRANSIENT	128
 * ACC_TRANSITIVE	32
 * ACC_VARARGS	128
 * ACC_VOLATILE	64
 * @author mrys
 */
  @SneakyThrows
  public static void main(String[] args) {
    Method[] methods = Test.class.getMethods();
    Method method = methods[0];
    Parameter[] parameters = method.getParameters();
    Parameter parameter = parameters[0];
    DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    String[] parameterNames = discoverer.getParameterNames(method);
    ClassReader cr = new ClassReader(Test.class.getName());
    cr.accept(new ClassVisitor(Opcodes.ASM7) {

      @Override
      public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
          String[] exceptions) {
        MethodVisitor methodVisitor = super
            .visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodVisitor(Opcodes.ASM7,methodVisitor) {
          @Override
          public void visitCode() {
            super.visitCode();
          }

          @Override
          public void visitParameter(String name, int access) {
            super.visitParameter(name, access);
          }

          @Override
          public void visitLocalVariable(String name, String descriptor, String signature,
              Label start, Label end, int index) {
            super.visitLocalVariable(name, descriptor, signature, start, end, index);
          }

          @Override
          public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
              boolean isInterface) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
          }

          @Override
          public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath,
              Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
            return super
                .visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor,
                    visible);
          }

          @Override
          public void visitEnd() {
            super.visitEnd();
          }
        };
      }
    },ClassReader.SKIP_FRAMES);
    System.out.println(cr.getAccess());
  }
}
