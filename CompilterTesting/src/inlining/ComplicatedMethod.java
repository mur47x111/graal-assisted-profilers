package inlining;

import static org.junit.Assert.assertEquals;
import inlining.target.Complicated;
import jdk.internal.jvmci.hotspot.DontInline;
import ch.usi.dag.testing.BaseTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class ComplicatedMethod extends BaseTestCase implements Constants {

	@DontInline
	@Override
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		Complicated o = new Complicated();
		o.caculate(RandomGen.nextInt());

		DelimitationAPI.instrumentationBegin(PRED);
		if (GraalQueryAPI.isMethodCompiled())
			counter++;
		DelimitationAPI.instrumentationEnd();
	}

	@Override
	public void verify() {
		assertEquals(counter, 0);
	}

}