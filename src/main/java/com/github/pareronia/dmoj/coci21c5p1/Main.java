package com.github.pareronia.dmoj.coci21c5p1;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * #1 - Kemija
 * @see <a href="https://dmoj.ca/problem/coci21c5p1">https://dmoj.ca/problem/coci21c5p1</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private Map<Character, Integer> buildMap(final String ss) {
        final Map<Character, Integer> map = new HashMap<>();
        final String[] ms = ss.split("\\+");
        for (final String m : ms) {
            int start;
            int factor;
            if (Character.isDigit(m.charAt(0))) {
                factor = Character.digit(m.charAt(0), 10);
                start = 1;
            } else {
                factor = 1;
                start = 0;
            }
            for (int j = start; j < m.length(); j++) {
                final Character c = m.charAt(j);
                if (Character.isLetter(c)) {
                    int cnt;
                    if (j < m.length() - 1 && Character.isDigit(m.charAt(j + 1))) {
                        cnt = Character.digit(m.charAt(j + 1), 10);
                        j++;
                    } else {
                        cnt = 1;
                    }
                    map.merge(c, factor * cnt, Integer::sum);
                }
            }
        }
        return map;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final String s = sc.next();
        final String[] ss = s.split("->");
        final Map<Character, Integer> left = buildMap(ss[0]);
        final Map<Character, Integer> right = buildMap(ss[1]);
        String ans;
        if (left.equals(right)) {
            ans = "DA";
        } else {
            ans = "NE";
        }
        this.out.println(ans);
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTests;
            if (isSample()) {
                numberOfTests = sc.nextInt();
            } else {
                numberOfTests = 1;
            }
            for (int i = 0; i < numberOfTests; i++) {
                final int numberOfTestCases = sc.nextInt();
                for (int j = 0; j < numberOfTestCases; j++) {
                    handleTestCase(j, sc);
                }
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
