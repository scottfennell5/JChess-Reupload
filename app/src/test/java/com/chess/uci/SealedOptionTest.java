package com.chess.uci;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.stream.Stream;

public class SealedOptionTest {
    private static Stream<Arguments> optionSource() {
        // Examples from Stockfish engine
        HashMap<String, SealedOption> map = new HashMap<>();
        map.put("option name Debug Log File type string default", new StringOption("Debug Log File", ""));
        map.put("option name Threads type spin default 1 min 1 max 512", new SpinOption("Threads", 1, 512, 1));
        map.put("option name Hash type spin default 16 min 1 max 33554432", new SpinOption("Hash", 1, 33554432, 16));
        map.put("option name Clear Hash type button", new ButtonOption("Clear Hash"));
        map.put("option name Ponder type check default false", new CheckOption("Ponder", false));
        map.put("option name MultiPV type spin default 1 min 1 max 500", new SpinOption("MultiPV", 1, 500, 1));
        map.put("option name Skill Level type spin default 20 min 0 max 20", new SpinOption("Skill Level", 0, 20, 20));
        map.put("option name Move Overhead type spin default 10 min 0 max 5000", new SpinOption("Move Overhead", 0,
            5000, 10));
        map.put("option name Slow Mover type spin default 100 min 10 max 1000", new SpinOption("Slow Mover", 10, 1000
            , 100));
        map.put("option name nodestime type spin default 0 min 0 max 10000", new SpinOption("nodestime", 0, 10000, 0));
        map.put("option name UCI_Chess960 type check default false", new CheckOption("UCI_Chess960", false));
        map.put("option name UCI_AnalyseMode type check default false", new CheckOption("UCI_AnalyseMode", false));
        map.put("option name UCI_LimitStrength type check default false", new CheckOption("UCI_LimitStrength", false));
        map.put("option name UCI_Elo type spin default 1350 min 1350 max 2850", new SpinOption("UCI_Elo", 1350, 2850,
            1350));
        map.put("option name UCI_ShowWDL type check default false", new CheckOption("UCI_ShowWDL", false));
        map.put("option name SyzygyPath type string default <empty>", new StringOption("SyzygyPath", "<empty>"));
        map.put("option name SyzygyProbeDepth type spin default 1 min 1 max 100", new SpinOption("SyzygyProbeDepth",
            1, 100, 1));
        map.put("option name Syzygy50MoveRule type check default true", new CheckOption("Syzygy50MoveRule", true));
        map.put("option name SyzygyProbeLimit type spin default 7 min 0 max 7", new SpinOption("SyzygyProbeLimit", 0,
            7, 7));
        map.put("option name Use NNUE type check default true", new CheckOption("Use NNUE", true));
        map.put("option name EvalFile type string default nn-6877cd24400e.nnue", new StringOption("EvalFile", "nn" +
            "-6877cd24400e.nnue"));
        map.put("option name WeightsFile type string default <autodiscover>", new StringOption("WeightsFile",
            "<autodiscover>"));
        map.put(
            "option name Backend type combo default opencl var opencl var blas var eigen var random var check var " +
                "recordreplay var roundrobin var multiplexing var demux",
            new ComboOption("Backend", new String[]{"opencl", "blas", "eigen", "random", "check", "recordreplay",
                "roundrobin", "multiplexing", "demux"}, "opencl")
        );
        // Examples from Leela Chess Zero (LC0) engine
        map.put("option name BackendOptions type string default", new StringOption("BackendOptions", ""));
        map.put("option name Threads type spin default 2 min 1 max 128", new SpinOption("Threads", 1, 128, 2));
        map.put("option name NNCacheSize type spin default 200000 min 0 max 999999999", new SpinOption("NNCacheSize",
            0, 999999999, 200000));
        map.put("option name MinibatchSize type spin default 256 min 1 max 1024", new SpinOption("MinibatchSize", 1,
            1024, 256));
        map.put("option name MaxPrefetch type spin default 32 min 0 max 1024", new SpinOption("MaxPrefetch", 0, 1024,
            32));
        map.put("option name CPuct type string default 1.745000", new StringOption("CPuct", "1.745000"));
        map.put("option name CPuctBase type string default 38739.000000", new StringOption("CPuctBase", "38739" +
            ".000000"));
        map.put("option name CPuctFactor type string default 3.894000", new StringOption("CPuctFactor", "3.894000"));
        map.put("option name TwoFoldDraws type check default true", new CheckOption("TwoFoldDraws", true));
        map.put("option name VerboseMoveStats type check default false", new CheckOption("VerboseMoveStats", false));
        map.put("option name FpuStrategy type combo default reduction var reduction var absolute", new ComboOption(
            "FpuStrategy", new String[]{"reduction", "absolute"}, "reduction"));
        map.put("option name FpuValue type string default 0.330000", new StringOption("FpuValue", "0.330000"));
        map.put("option name CacheHistoryLength type spin default 0 min 0 max 7", new SpinOption("CacheHistoryLength"
            , 0, 7, 0));
        map.put("option name PolicyTemperature type string default 1.359000", new StringOption("PolicyTemperature",
            "1.359000"));
        map.put("option name MaxCollisionEvents type spin default 917 min 1 max 65536", new SpinOption(
            "MaxCollisionEvents", 1, 65536, 917));
        map.put("option name MaxCollisionVisits type spin default 80000 min 1 max 100000000", new SpinOption(
            "MaxCollisionVisits", 1, 100000000, 80000));
        map.put("option name MaxCollisionVisitsScalingStart type spin default 28 min 1 max 100000", new SpinOption(
            "MaxCollisionVisitsScalingStart", 1, 100000, 28));
        map.put("option name MaxCollisionVisitsScalingEnd type spin default 145000 min 0 max 100000000",
            new SpinOption("MaxCollisionVisitsScalingEnd", 0, 100000000, 145000));
        map.put("option name MaxCollisionVisitsScalingPower type string default 1.250000", new StringOption(
            "MaxCollisionVisitsScalingPower", "1.250000"));
        map.put("option name OutOfOrderEval type check default true", new CheckOption("OutOfOrderEval", true));
        map.put("option name MaxOutOfOrderEvalsFactor type string default 2.400000", new StringOption(
            "MaxOutOfOrderEvalsFactor",
            "2.400000"));
        map.put("option name StickyEndgames type check default true", new CheckOption("StickyEndgames", true));
        map.put("option name SyzygyFastPlay type check default false", new CheckOption("SyzygyFastPlay", false));
        map.put("option name PerPVCounters type check default false", new CheckOption("PerPVCounters", false));
        map.put("option name ScoreType type combo default centipawn var centipawn var centipawn_with_drawscore var " +
                "centipawn_2019 var centipawn_2018 var win_percentage var Q var W-L",
            new ComboOption("ScoreType", new String[]{"centipawn", "centipawn_with_drawscore", "centipawn_2019",
                "centipawn_2018", "win_percentage", "Q", "W-L"}, "centipawn"));
        map.put("option name HistoryFill type combo default fen_only var no var fen_only var always",
            new ComboOption("HistoryFill", new String[]{"no", "fen_only", "always"}, "fen_only"));
        map.put("option name MovesLeftMaxEffect type string default 0.034500", new StringOption("MovesLeftMaxEffect",
            "0.034500"));
        map.put("option name MovesLeftThreshold type string default 0.000000", new StringOption("MovesLeftThreshold",
            "0.000000"));
        map.put("option name MovesLeftSlope type string default 0.002700", new StringOption("MovesLeftSlope", "0" +
            ".002700"));
        map.put("option name MovesLeftConstantFactor type string default 0.000000", new StringOption(
            "MovesLeftConstantFactor", "0.000000"));
        map.put("option name MovesLeftScaledFactor type string default 1.652100", new StringOption(
            "MovesLeftScaledFactor", "1.652100"));
        map.put("option name MovesLeftQuadraticFactor type string default -0.652100", new StringOption(
            "MovesLeftQuadraticFactor", "-0.652100"));
        map.put("option name MaxConcurrentSearchers type spin default 1 min 0 max 128", new SpinOption(
            "MaxConcurrentSearchers", 0, 128, 1));
        map.put("option name DrawScoreSideToMove type spin default 0 min -100 max 100", new SpinOption(
            "DrawScoreSideToMove", -100, 100, 0));
        map.put("option name DrawScoreOpponent type spin default 0 min -100 max 100", new SpinOption(
            "DrawScoreOpponent", -100, 100, 0));
        map.put("option name DrawScoreWhite type spin default 0 min -100 max 100", new SpinOption("DrawScoreWhite",
            -100, 100, 0));
        map.put("option name DrawScoreBlack type spin default 0 min -100 max 100", new SpinOption("DrawScoreBlack",
            -100, 100, 0));
        map.put("option name NodesPerSecondLimit type string default 0.000000", new StringOption("NodesPerSecondLimit"
            , "0.000000"));
        map.put("option name SolidTreeThreshold type spin default 100 min 1 max 2000000000", new SpinOption(
            "SolidTreeThreshold", 1, 2000000000, 100));
        map.put("option name MultiGather type check default true", new CheckOption("MultiGather", true));
        map.put("option name TaskWorkers type spin default 4 min 0 max 128", new SpinOption("TaskWorkers", 0, 128, 4));
        map.put("option name MinimumProcessingWork type spin default 20 min 2 max 100000", new SpinOption(
            "MinimumProcessingWork", 2, 100000, 20));
        map.put("option name MinimumPickingWork type spin default 1 min 1 max 100000", new SpinOption(
            "MinimumPickingWork", 1, 100000, 1));
        map.put("option name MinimumRemainingPickingWork type spin default 20 min 0 max 100000", new SpinOption(
            "MinimumRemainingPickingWork", 0, 100000, 20));
        map.put("option name MinimumPerTaskProcessing type spin default 8 min 1 max 100000", new SpinOption(
            "MinimumPerTaskProcessing", 1, 100000, 8));
        map.put("option name IdlingMinimumWork type spin default 0 min 0 max 10000", new SpinOption(
            "IdlingMinimumWork", 0, 10000, 0));
        map.put("option name SyzygyPath type string default", new StringOption("SyzygyPath", ""));
        map.put("option name Ponder type check default true", new CheckOption("Ponder", true));
        map.put("option name UCI_ShowMovesLeft type check default false", new CheckOption("UCI_ShowMovesLeft", false));
        map.put("option name ConfigFile type string default lc0.config", new StringOption("ConfigFile", "lc0.config"));
        map.put("option name SmartPruningFactor type string default 1.330000", new StringOption("SmartPruningFactor",
            "1.330000"));
        map.put("option name SmartPruningMinimumBatches type spin default 0 min 0 max 10000", new SpinOption(
            "SmartPruningMinimumBatches", 0, 10000, 0));
        map.put("option name RamLimitMb type spin default 0 min 0 max 100000000", new SpinOption("RamLimitMb", 0,
            100000000, 0));
        map.put("option name MoveOverheadMs type spin default 200 min 0 max 100000000", new SpinOption(
            "MoveOverheadMs", 0, 100000000, 200));
        map.put("option name TimeManager type string default legacy", new StringOption("TimeManager", "legacy"));
        map.put("option name LogFile type string default", new StringOption("LogFile", ""));
        return map.entrySet().stream().map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }

    @ParameterizedTest
    @MethodSource("optionSource")
    public void testOptions(String option, SealedOption expected) {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, SealedOption.parse(option)));
        Assertions.assertEquals(option, SealedOption.parse(option).toString());
    }
}
