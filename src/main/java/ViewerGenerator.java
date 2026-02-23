import java.util.Map;

public class ViewerGenerator {
    public static String generateViewer(String Name, int level, Map<String, Object> ranking, Map<String, Object> codingTest, int careerScore, int registerPeriod) {
        // 정답률 계산
        int solved = (int) codingTest.get("solved");
        int total = (int) codingTest.get("total");
        double accuracy = total > 0 ? (double) solved / total * 100 : 0.0;
        accuracy = Math.round(accuracy * 10) / 10.0; // 소수점 한 자리까지 반올림

        // 랭킹 가져오기
        String score = String.format("%,d", (int) ranking.get("score"));
        String rank = String.format("%,d", (int) ranking.get("rank"));

        // 레벨 채워지는 정도 계산
        int fillPercentage = (int) (220 * (1 - (level + 1f) / 6f));

        String template = """
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="480" height="245"
    style="shape-rendering:geometricPrecision; text-rendering:geometricPrecision; image-rendering:optimizeQuality; fill-rule:evenodd; clip-rule:evenodd"
    xmlns:xlink="http://www.w3.org/1999/xlink">
    <style>
        .header {
            font: 600 17px 'Segoe UI', Ubuntu, Sans-Serif;
            fill: #58a6ff;
            animation: fadeInAnimation 0.8s ease-in-out forwards;
        }

        .subheader {
            font: 400 11px 'Segoe UI', Ubuntu, Sans-Serif;
            fill: #8b949e;
            animation: fadeInAnimation 0.8s ease-in-out forwards;
        }

        .stat {
            font: 600 13px 'Segoe UI', Ubuntu, "Helvetica Neue", Sans-Serif;
            fill: #c9d1d9;
        }

        .stat-value {
            font: 700 13px 'Segoe UI', Ubuntu, "Helvetica Neue", Sans-Serif;
            fill: #58a6ff;
        }

        @supports(-moz-appearance: auto) {
            .stat { font-size: 11px; }
            .stat-value { font-size: 11px; }
        }

        .stagger {
            opacity: 0;
            animation: fadeInAnimation 0.3s ease-in-out forwards;
        }

        .rank-text {
            font: 800 20px 'Segoe UI', Ubuntu, Sans-Serif;
            fill: #c9d1d9;
            animation: scaleInAnimation 0.3s ease-in-out forwards;
        }

        .divider {
            stroke: #30363d;
            stroke-width: 1;
        }

        .rank-circle-rim {
            stroke: #58a6ff;
            fill: none;
            stroke-width: 5;
            opacity: 0.15;
        }

        .rank-circle {
            stroke: #58a6ff;
            stroke-dasharray: 220;
            fill: none;
            stroke-width: 5;
            stroke-linecap: round;
            opacity: 0.85;
            transform-origin: -10px 8px;
            transform: rotate(-90deg);
            animation: rankAnimation 1.2s forwards ease-in-out;
        }

        /* rank=50125 → relatively low → let's show ~15%% filled */
        @keyframes rankAnimation {
            from { stroke-dashoffset: 220; }
            to   { stroke-dashoffset: %s; }
        }

        /* progress bar */
        .bar-bg {
            fill: #21262d;
            rx: 3;
        }
        .bar-fill {
            fill: #58a6ff;
            rx: 3;
            animation: barGrow 1s ease-in-out forwards;
        }
        .bar-fill-green {
            fill: #3fb950;
            rx: 3;
            animation: barGrow 1s ease-in-out forwards;
        }

        @keyframes barGrow {
            from { width: 0; }
        }

        @keyframes scaleInAnimation {
            from { transform: translate(-5px, 5px) scale(0); }
            to   { transform: translate(-5px, 5px) scale(1); }
        }

        @keyframes fadeInAnimation {
            from { opacity: 0; }
            to   { opacity: 1; }
        }
    </style>

    <!-- Card background -->
    <rect x="0.5" y="0.5" rx="6" height="99%%" stroke="#30363d" width="479" fill="#0d1117" stroke-opacity="1" />

    <!-- Header -->
    <g transform="translate(25, 35)">
        <text x="0" y="0" class="header">%s's Programmers Stats</text>

    </g>

    <!-- Divider -->
    <line x1="25" y1="52" x2="455" y2="52" class="divider" />

    <!-- Rank circle (top-right) -->
    <g transform="translate(400, 105)">
        <circle class="rank-circle-rim" cx="-10" cy="8" r="35" />
        <circle class="rank-circle" cx="-10" cy="8" r="35" />
        <g class="rank-text">
            <text x="-5" alignment-baseline="central" dominant-baseline="central" text-anchor="middle">Lv.%s</text>
        </g>
        <text class="stat" x="-9" y="58" alignment-baseline="central" dominant-baseline="central" text-anchor="middle">정복한 레벨</text>
    </g>

    <!-- Stats rows -->
    <g transform="translate(0, 65)">

        <!-- Score -->
        <g class="stagger" style="animation-delay: 300ms" transform="translate(25, 0)">
            <text class="stat" y="12.5">현 랭킹 점수:</text>
            <text class="stat-value" x="180" y="12.5">%s점</text>
        </g>

        <!-- Rank -->
        <g class="stagger" style="animation-delay: 450ms" transform="translate(25, 27)">
            <text class="stat" y="12.5">현 순위:</text>
            <text class="stat-value" x="180" y="12.5">%s위</text>
        </g>

        <!-- Coding test 정답률 -->
        <g class="stagger" style="animation-delay: 600ms" transform="translate(25, 54)">
            <text class="stat" y="12.5">코딩테스트 정답률:</text>
            <text class="stat-value" x="180" y="12.5">%s%%</text>
        </g>

        <!-- Coding test only solved -->
        <g class="stagger" style="animation-delay: 750ms" transform="translate(25, 81)">
            <text class="stat" y="12.5">해결한 코딩테스트:</text>
            <text class="stat-value" x="180" y="12.5">%s 문제</text>
        </g>

        <!-- Career -->
        <g class="stagger" style="animation-delay: 900ms" transform="translate(25, 108)">
            <text class="stat" y="12.5">경력 점수:</text>
            <text class="stat-value" x="180" y="12.5">%s점</text>
        </g>

        <!-- Register period -->
        <g class="stagger" style="animation-delay: 1050ms" transform="translate(25, 132)">
            <text class="stat" y="12.5">가입 기간:</text>
            <text class="stat-value" x="180" y="12.5">%s일</text>
        </g>

    </g>

    <!-- Bottom divider -->
    <line x1="25" y1="220" x2="455" y2="220" class="divider" />
    <text x="25" y="235" class="subheader">Programmers Stats Viewer (rrayy-25809 Ver.)</text>
</svg>
                """;;
        // 순서: 레벨 채워지는 정도, 이름, 레벨, 점수, 순위, 정답률, 해결한 문제 수, 경력 점수, 가입 기간
        return String.format(template, fillPercentage, Name, String.format("%,d", level), score, rank, accuracy, String.format("%,d", solved), String.format("%,d", careerScore), String.format("%,d", registerPeriod));
    }
}
