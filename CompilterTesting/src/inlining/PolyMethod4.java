package inlining;

import static org.junit.Assert.assertEquals;
import inlining.target.Factory;
import inlining.target.Super;
import jdk.internal.jvmci.hotspot.DontInline;
import ch.usi.dag.testing.BaseTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class PolyMethod4 extends BaseTestCase implements Constants {

	@DontInline
	@Override
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		Super o = Factory.nextSuper();
		o.caculate(RandomGen.nextInt());

		DelimitationAPI.instrumentationBegin(PRED);
		if (GraalQueryAPI.isMethodCompiled())
			counter++;
		DelimitationAPI.instrumentationEnd();
	}

	@Override
	public void verify() {
		assertEquals(counter, ITERATIONS);
	}

}