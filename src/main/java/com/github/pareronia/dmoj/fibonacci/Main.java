package com.github.pareronia.dmoj.fibonacci;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Fibonacci Sequence
 * @see <a href="https://dmoj.ca/problem/fibonacci">https://dmoj.ca/problem/fibonacci</a>
 */
public class Main {

    private final boolean sample;
    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.sample = sample;
        this.in = in;
        this.out = out;
    }
    
    private static class Modular {
        
        private static long modularAbs(final long n, final long mod) {
            assert mod > 0;
            long nn = n % mod;
            if (nn < 0) {
                nn += mod;
            }
            return nn;
        }

        public static long add(final long a, final long b, final long mod) {
            final long aa = modularAbs(a, mod);
            final long bb = modularAbs(b, mod);
            if (bb > mod - aa) {
                return bb - (mod - aa);
            }
            return (aa + bb) % mod;
        }
        
        public static long multiply(final long a, final long b, final long mod) {
            final long aa = modularAbs(a, mod);
            final long bb = modularAbs(b, mod);
            if (bb == 0) {
                return 0;
            }
            return add(
                    multiply(
                            add(aa, aa, mod),
                            bb / 2,
                            mod),
                    (bb % 2 == 1 ? aa : 0),
                    mod);
        }
        
        public static long subtract(final long a, final long b, final long mod) {
            return add(a, -b, mod);
        }
    }
    
    private static class Fibonacci {
        
        public static Pair<Long, Long> fast(final long n, final long mod) {
            if (n == 0) {
                return Pair.of(0L, 1L);
            }
            final Pair<Long, Long> p = fast(n >> 1, mod);
            final long p1 = p.getOne();
            final long p2 = p.getTwo();
            final long c1 = Modular.subtract(Modular.multiply(2, p2, mod), p1, mod);
            final long c = Modular.multiply(p1, c1, mod);
            final long d1 = Modular.multiply(p1, p1, mod);
            final long d2 = Modular.multiply(p2, p2, mod);
            final long d = Modular.add(d1, d2, mod);
            if ((n & 1) == 1) {
                return Pair.of(d, Modular.add(c, d, mod));
            } else {
                return Pair.of(c, d);
            }
        }
        
        public static final class Pair<L, R> {
            private final L one;
            private final R two;

            private Pair(final L one, final R two) {
                this.one = one;
                this.two = two;
            }

            public static <L, R> Pair<L, R> of(final L one, final R two) {
                return new Pair<>(one, two);
            }

            @Override
            public int hashCode() {
                return Objects.hash(one, two);
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean equals(final Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final Pair<L, R> other = (Pair<L, R>) obj;
                return Objects.equals(one, other.one) && Objects.equals(two, other.two);
            }

            @Override
            public String toString() {
                final StringBuilder builder = new StringBuilder();
                builder.append("Pair [one=").append(one).append(", two=").append(two).append("]");
                return builder.toString();
            }

            public L getOne() {
                return one;
            }
            
            public R getTwo() {
                return two;
            }
        }
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final long n = sc.nextLong();
        this.out.println(Fibonacci.fast(n, 1_000_000_007).getOne());
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases;
            if (this.sample) {
                numberOfTestCases = sc.nextInt();
            } else {
                numberOfTestCases = 1;
            }
            for (int i = 1; i <= numberOfTestCases; i++) {
                handleTestCase(i, sc);
            }
        }
    }

    public static void main(final String[] args) throws IOException, URISyntaxException {
        final boolean sample = isSample();
        final InputStream is;
        final PrintStream out;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long timerStart = 0;
        if (sample) {
            is = Main.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new Main(sample, is, out).solve();
        
        out.flush();
        if (sample) {
            final long timeSpent = (System.nanoTime() - timerStart) / 1_000;
            final double time;
            final String unit;
            if (timeSpent < 1_000) {
                time = timeSpent;
                unit = "µs";
            } else if (timeSpent < 1_000_000) {
                time = timeSpent / 1_000.0;
                unit = "ms";
            } else {
                time = timeSpent / 1_000_000.0;
                unit = "s";
            }
            final Path path
                    = Paths.get(Main.class.getResource("sample.out").toURI());
            final List<String> expected = Files.readAllLines(path);
            final List<String> actual = asList(baos.toString().split("\\r?\\n"));
            if (!expected.equals(actual)) {
                throw new AssertionError(String.format(
                        "Expected %s, got %s", expected, actual));
            }
            actual.forEach(System.out::println);
            System.out.println(String.format("took: %.3f %s", time, unit));
        }
    }
    
    private static boolean isSample() {
        try {
            return "sample".equals(System.getProperty("dmoj"));
        } catch (final SecurityException e) {
            return false;
        }
    }
    
    private static final class FastScanner implements Closeable {
        private final BufferedReader br;
        private StringTokenizer st;
        
        public FastScanner(final InputStream in) {
            this.br = new BufferedReader(new InputStreamReader(in));
            st = new StringTokenizer("");
        }
        
        public String next() {
            while (!st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }
    
        public int nextInt() {
            return Integer.parseInt(next());
        }
        
        public long nextLong() {
            return Long.parseLong(next());
        }

        @Override
        public void close() {
            try {
                this.br.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }
}
