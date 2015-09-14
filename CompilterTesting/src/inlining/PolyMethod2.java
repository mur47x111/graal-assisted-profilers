package inlining;

import inlining.target.Complicated;
import inlining.target.Simple;
import inlining.target.Super;
import jdk.internal.jvmci.hotspot.DontInline;
import ch.usi.dag.testing.BaseTestCase;

import com.oracle.graal.debug.query.DelimitationAPI;
import com.oracle.graal.debug.query.GraalQueryAPI;

public class PolyMethod2 extends BaseTestCase implements Constants {

	@DontInline
	@Override
	public void target() {
		DelimitationAPI.instrumentationBegin(HERE);
		if (GraalQueryAPI.isMethodCompiled())
			isCompiled = true;
		DelimitationAPI.instrumentationEnd();

		Super o = likely(LIKELY) ? new Simple() : new Complicated();

		if (likely(UNLIKELY)) {
			o.calculate(RandomGen.nextInt());

			DelimitationAPI.instrumentationBegin(PRED);
			if (GraalQueryAPI.isMethodCompiled())
				counter++;
			DelimitationAPI.instrumentationEnd();
		}
	}

	@Override
	public double expectedRatio() {
		return NOT_INLINE * UNLIKELY;
	}

}
