package com.github.pareronia.dmoj.coci21c5p2;

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
 * #2 - Dijament
 * @see <a href="https://dmoj.ca/problem/coci21c5p2">https://dmoj.ca/problem/coci21c5p2</a>
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
        final int m = sc.nextInt();
        final char[][] g = new char[n][m];
        for (int j = 0; j < n; j++) {
            g[j] = sc.next().toCharArray();
        }
        int ans = 0;
        for (int r = 0; r < n - 2; r++) {
            outer:
            for (int c = 1; c < m - 1; c++) {
                if (g[r][c] == '#' && g[r + 1][c] == '.') {
                    int top = r;
                    int bottom = findBottom(g, r, c);
                    if (bottom == -1) {
                        continue;
                    }
                    final int size = (bottom - top + 1) / 2;
                    for (int j = 1; j <= size; j++) {
                        top++;
                        bottom--;
                        int col = c - j;
                        if (col < 0 || !check(g, col, top, bottom)) {
                            continue outer;
                        }
                        col = c + j;
                        if (col > m - 1 || !check(g, col, top, bottom)) {
                            continue outer;
                        }
                    }
                    ans++;
                }
            }
        }
        this.out.println(ans);
    }
    
    private int findBottom(final char[][] g, final int row, final int col) {
        int bottom = -1;
        int rr = row + 1;
        while (rr < g.length) {
            if (g[rr][col] == '#') {
                bottom = rr;
                break;
            }
            rr++;
        }
        return bottom;
    }
    
    private boolean check(final char[][] g, final int col, final int rowfrom, final int rowto) {
        if (g[rowfrom][col] != '#') {
            return false;
        }
        if (g[rowto][col] != '#') {
            return false;
        }
        for (int r = rowfrom + 1; r <= rowto - 1; r++) {
            if (r >= g.length) {
                return false;
            }
            if (g[r][col] != '.') {
                return false;
            }
        }
        return true;
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases;
            if (isSample()) {
                numberOfTestCases = sc.nextInt();
            } else {
                numberOfTestCases = 1;
            }
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
