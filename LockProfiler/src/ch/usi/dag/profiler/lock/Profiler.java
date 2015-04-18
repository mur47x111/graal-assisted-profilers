package ch.usi.dag.profiler.lock;

import ch.usi.dag.profiler.dump.ArchiveDumper;
import ch.usi.dag.profiler.dump.Dumper;
import ch.usi.dag.profiler.meta.ConcurrentCounterMap;

public class Profiler {
	
	public static final int CASE = 9;
	// case 0: error
	// case 1: +lock{stub:revoke}
	// case 2: +lock{stub:epoch-expired}
	// case 3: +lock{stub:failed-cas}
	// case 4: +lock{bias:existing}
	// case 5: +lock{bias:acquired}
	// case 6: +lock{bias:transfer}
	// case 7: +lock{recursive}
	// case 8: +lock{cas}

	public static final ConcurrentCounterMap counters[] = new ConcurrentCounterMap[CASE];

	static {
		initProfile();
	}

	public static void initProfile() {
		for (int i = 0; i < CASE; i++) {
			counters[i] = new ConcurrentCounterMap();
		}
	}

	public static void clearProfile() {
		for (int i = 0; i < CASE; i++) {
			counters[i].clear();
		}
	}

	public static void dumpProfile(String name) {
		try (Dumper dumper = new ArchiveDumper(name)) {
			ConcurrentCounterMap.compare(dumper, counters);
		}
	}

	public static void profileLock(String key, int type) {
		counters[type].increment(key);
	}

}