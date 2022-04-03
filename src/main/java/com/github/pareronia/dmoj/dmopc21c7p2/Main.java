package com.github.pareronia.dmoj.dmopc21c7p2;

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
import java.util.StringTokenizer;

/**
 * P2 - Knitting Scarves
 * @see <a href="https://dmoj.ca/problem/dmopc21c7p2">https://dmoj.ca/problem/dmopc21c7p2</a>
 */
public class Main {

    private final InputStream in;
    private final PrintStream out;
    
    public Main(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int q = sc.nextInt();
        final int[][] b = new int [n + 1][2];
        int bhead = 1;
        b[1] = new int[] { -1, -1 };
        int bprev = 1;
        for (int j = 2; j < n; j++) {
            b[j] = new int[] { -1, -1 };
            b[j][0] = bprev;
            b[bprev][1] = j;
            bprev = j;
        }
        b[n] = new int[] { -1, -1 };
        b[n][0] = bprev;
        b[bprev][1] = n;
        b[n][1] = 1;
        b[1][0] = n;
        for (int j = 0; j < q; j++) {
            final int l = sc.nextInt();
            final int r = sc.nextInt();
            final int k = sc.nextInt();
            final int[] bstart = b[l];
            final int[] bend = b[r];
            b[bstart[0]][1] = bend[1];
            b[bend[1]][0] = bstart[0];
            if (k > 0) {
                final int[] btarget = b[k];
                if (l == bhead) {
                    bhead = bend[1];
                }
                b[btarget[1]][0] = r;
                bend[1] = btarget[1];
                btarget[1] = l;
                bstart[0] = k;
            } else {
                b[b[bhead][0]][1] = l;
                b[l][0] = b[bhead][0];
                b[bhead][0] = r;
                bend[1] = bhead;
                bhead = l;
            }
        }
        int c = bhead;
        while (true) {
            this.out.print(c);
            c = b[c][1];
            if (c == bhead) {
                break;
            }
            this.out.print(" ");
        }
        this.out.println();
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases = isSample() ? sc.nextInt() : 1;
            for (int i = 0; i < numberOfTestCases; i++) {
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
