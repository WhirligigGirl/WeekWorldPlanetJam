package tholin.wwpj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

import ddsutil.DDSUtil;
import edu.cornell.lassp.houle.RngPack.RanMT;
import jogl.DDSImage;
import tholin.planetGen.generators.AsteroidMoonGen;
import tholin.planetGen.generators.AsteroidMoonGen.AsteroidGenSettings;
import tholin.planetGen.generators.GeneratorResult;
import tholin.planetGen.generators.GraymoonGen;
import tholin.planetGen.noise.*;
import tholin.planetGen.utils.*;
import tholin.planetGen.utils.CraterDistributer.CraterDistributionSettings;
import tholin.planetGen.utils.CraterGenerator.CraterConfig;
import tholin.planetGen.utils.FeatureDistributer.FeatureDistributerConfig;
import tholin.planetGen.utils.RavineGenerator.RavineConfig;

public class Gens {
	public static void main(String[] args) {
		try {
			genNoir(8192, 251326216146_000L);
			genKalt(8192, 2843925823623L);
			genTris(8192, 21501325887L);
			genNik(8192, 38512365216L);
			genNak(8192, 235623626L);
		}catch(Exception e) {
			System.err.println("Error:");
			e.printStackTrace();
			System.exit(1);
		}
		NoisemapGenerator.cleanUp();
		FeatureDistributer.cleanUp();
	}

	public static void genNak(int width, long seed) throws Exception {
		int height = width / 2;
		RanMT rng = new RanMT().seedCompletely(new Random(seed));

		AsteroidMoonGen.AsteroidGenSettings settings = new AsteroidMoonGen.AsteroidGenSettings();
		settings.width = settings.colorMapWidth = width;
		settings.height = settings.colorMapHeight = height;

		settings.shapeNoise.setIsRidged(false).setNoiseStrength(1.9).setNoiseLatitudeScale(2.0).setNoiseLongitudeScale(1.5).setNoiseOffset(0.25).setDistortStrength(0.1).noise = new OctaveNoise3D(4, 2.0, 0.6);
		settings.groundNoise.setIsRidged(false).setNoiseStrength(0.1).setNoiseLatitudeScale(0.8).setNoiseLongitudeScale(0.8).setNoiseOffset(0.2);
		settings.peakNoise.setNoiseStrength(0.1).setNoiseScale(1.2).setNoiseOffset(0);
		settings.secondaryNoise.setNoiseLatitudeScale(0.5).setNoiseLongitudeScale(0.5).setNoiseStrength(0.04);
		settings.secondColorNoise.setNoiseScale(1);
		settings.colorNoise.noise = new OctaveWorley(4, 2.0, 0.5);
		settings.colorNoise.setNoiseScale(0.2).setDistortStrength(0.4);

		settings.craterCount = 256;
		settings.craterMaxsize = 16;
		settings.craterMaxstrength = 0.025;
		settings.craterMinsize = 4;
		settings.craterMinstrength = 0.0125;
		settings.craterConfig.p1 *= 1.25;
		settings.craterConfig.floorHeight = -0.85;

		settings.normalColor = MapUtils.RGB(new Color(8, 8, 32));
		settings.peaksColor = MapUtils.RGB(new Color(36, 120, 175));
		settings.secondaryColor = MapUtils.RGB(new Color(50, 50, 50));

		GeneratorResult res = new AsteroidMoonGen().generate(rng, settings, true, false);
		ImageIO.write(res.heightmap16, "png", new File("Nak_height.png"));
		DDSUtil.write(new File("Nak_colors.dds"), flipImageHorizontal(res.colorMap), DDSImage.D3DFMT_DXT1, false);
		ImageIO.write(res.biomeMap, "png", new File("Nak_biomes.png"));
		BufferedImage nm = MapUtils.generateNormalMap(res.heightmapRaw, 16555, 8000, 1.0 / 3.0);
		for(int i = 0; i < nm.getWidth(); i++) for(int j = 0; j < nm.getHeight(); j++) {
			nm.setRGB(i, j, nm.getRGB(i, j) | 0x00FF00FF);
		}
		DDSUtil.write(new File("Nak_normals.dds"), flipImageHorizontal(nm), DDSImage.D3DFMT_DXT5, false);
	}

	public static void genNik(int width, long seed) throws Exception {
		int height = width / 2;
		RanMT rng = new RanMT().seedCompletely(new Random(seed));

		AsteroidMoonGen.AsteroidGenSettings settings = new AsteroidMoonGen.AsteroidGenSettings();
		settings.width = settings.colorMapWidth = width;
		settings.height = settings.colorMapHeight = height;

		settings.shapeNoise.setIsRidged(false).setNoiseStrength(2.3).setNoiseLatitudeScale(2.0).setNoiseLongitudeScale(1.5).setNoiseOffset(0.1).noise = new OctaveNoise3D(5, 2.0, 0.6);
		settings.groundNoise.setIsRidged(false).setNoiseStrength(0.2).setNoiseScale(0.621).setNoiseOffset(0.3);
		settings.peakNoise.setNoiseStrength(0.1).setNoiseScale(0.8).setNoiseOffset(0);
		settings.secondaryNoise.setNoiseLatitudeScale(0.5).setNoiseLongitudeScale(0.621);
		settings.secondColorNoise.setNoiseScale(2);
		settings.colorNoise.noise = new OctaveWorley(6, 2.0, 0.5);
		settings.colorNoise.setNoiseScale(0.2).setWiggleDensity(1.77777).setDistortStrength(0.82);

		settings.craterCount = 256;
		settings.craterMaxsize = 16;
		settings.craterMaxstrength = 0.025;
		settings.craterMinsize = 2;
		settings.craterMinstrength = 0.00625;
		settings.craterConfig.p1 *= 1.25;
		settings.craterConfig.floorHeight = -0.85;

		settings.normalColor = MapUtils.RGB(new Color(8, 8, 8));
		settings.peaksColor = MapUtils.RGB(new Color(232, 162, 134));
		settings.secondaryColor = MapUtils.RGB(new Color(232, 90, 80));

		GeneratorResult res = new AsteroidMoonGen().generate(rng, settings, true, false);
		ImageIO.write(res.heightmap16, "png", new File("Nik_height.png"));
		DDSUtil.write(new File("Nik_colors.dds"), flipImageHorizontal(res.colorMap), DDSImage.D3DFMT_DXT1, false);
		ImageIO.write(res.biomeMap, "png", new File("Nik_biomes.png"));
		BufferedImage nm = MapUtils.generateNormalMap(res.heightmapRaw, 28111, 9000, 1.0 / 3.0);
		for(int i = 0; i < nm.getWidth(); i++) for(int j = 0; j < nm.getHeight(); j++) {
			nm.setRGB(i, j, nm.getRGB(i, j) | 0x00FF00FF);
		}
		DDSUtil.write(new File("Nik_normals.dds"), flipImageHorizontal(nm), DDSImage.D3DFMT_DXT5, false);
	}

	public static void genTris(int w, long seed) throws Exception {
		final int width = w;
		final int height = w / 2;

		final int mariaShapeCraterCount = 4;
		CraterConfig mariaShapeCraterConfig = new CraterConfig()
						.setPerturbStrength(0.42).setPerturbScale(0.45).setP1(1.0).setP2(8.4).setFloorHeight(-0.5)
						.setEjectaStrength(1).setEjectaPerturbScale(0).setEjectaStretch(0).setEjectaPerturbStrength(0)
						.setFullPeakSize(10).setRingThreshold(128).setRingFunctMul(1);

		final double ridgeMaxlength = Math.PI / 8.0;
		final double ridgeMinlength = Math.PI / 45.0;
		final double ridgeMaxsize = 8;
		final double ridgeMinsize = 2;
		final double ridgeMaxstrength = 0.5;
		final double ridgeMinstrength = 0.1;
		final int ridgeCount = 12;

		//Values I shamelessly copy-pasted from Ralu's generator
		final double craterMaxsize = 64;
		final double craterMinsize = 4;
		final double craterMaxstrength = 0.4;
		final double craterMinstrength = 0.03;
		final int    craterCount = 400;
		final double[] craterRimFades = new double[] {0.4, 0.4, 0.4};
		final double craterRimFadeStart = 0.0075;
		final double craterRimFadeEnd = 0.055;
		final double mariaCraterMaxsize = 6;
		final double mariaCraterMinsize = 2;
		final double mariaCraterMaxstrength = 0.045;
		final double mariaCraterMinstrength = 0.028;
		final int    mariaCraterCount = 300;

		RanMT rng = new RanMT().seedCompletely(new Random(seed));

		double[][] cellsMap = new double[width][height];
		double[][] temp = new double[width][height];
		double[][] temp2 = new double[width][height];
		double[][] temp3 = new double[width][height];
		double[][] maria = new double[width][height];
		double[][] finalHeightmap = new double[width][height];
		double[][] craterMap = new double[width][height];
		boolean[][] craterDistrThingy = new boolean[width][height];
		boolean[][] mariaCraterDistrThingy = new boolean[width][height];

		System.out.println("Maria");
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) maria[i][j] = 1.0;

		RanMT mRng = new RanMT().seedCompletely(rng);
		CraterGenerator craterGen = new CraterGenerator(width, height);
		for(int i = 0; i < mariaShapeCraterCount; i++) {
			double lat = (mRng.nextDouble() * 0.8 - 0.4) * 90.0;
			double lon = (mRng.nextDouble() * 2 - 1) * 180.0;

			int px = (int)((lon + 180.0) / 360.0 * width);
			int py = (int)((lat + 90.0) / 180.0 * height);
			double val = maria[px][py];
			if(val < 1) {
				i--;
				continue;
			}
			mariaShapeCraterConfig.setSize(300 + mRng.nextInt(64)).setCraterStrength(0.85);
			craterGen.genCrater(maria, null, 0, maria[0].length, lat, lon, mariaShapeCraterConfig, null, mRng);
		}
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) temp[i][j] = maria[i][j];
		//PostProcessingEffects.gaussianBlur(temp, maria, 1.0 / (double)maria.length, 1.0 / (double)maria[0].length, (int)(32.0 * (double)width / 4096.0));
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			double d;
			if(maria[i][j] < 0.9) d = 0;
			else d = ((maria[i][j] - 0.9) / 0.1);
			maria[i][j] = Maths.biasFunction(Math.min(1, d), -0.6);
		}

		RanMT ridgeRng = new RanMT().seedCompletely(rng);
		NoiseConfig defaultRavineDistortNoise = new NoiseConfig(new OctaveNoise2D(6, 6, 4, 1.6, 0.6), false, 3, 15, 0.222, 3.7, 0, 0);
		NoiseConfig defaultRavineRimNoise = new NoiseConfig(new OctaveNoise3D(24, 24, 24, 10, 2.0, 0.65)).setIsRidged(true).setNoiseStrength(1.0 / 0.23).setNoiseScale(0.72).setDistortStrength(0.43).setNoiseOffset(0);
		defaultRavineRimNoise.noise.initialize(new RanMT().seedCompletely(rng));
		RavineGenerator gen = new RavineGenerator(width, height);
		ProgressBars.printBar();
		for(int i = 0; i < width; i++) {
			Arrays.fill(temp[i], 0.75);
			Arrays.fill(temp2[i], 1.0);
		}
		for(int i = 0; i < ridgeCount; i++) {
			ProgressBars.printProgress(i, ridgeCount - 1);
			double targetLen = Maths.biasFunction(ridgeRng.nextDouble(), 0.3) * (ridgeMaxlength - ridgeMinlength - 0.005) + ridgeMinlength;

			double lat = 180.0 + (rng.nextDouble() * 2.0 - 1.0) * 60.0;
			double lon = ridgeRng.nextDouble() * 340.0 + 10.0 - 180.0;
			double lat2,lon2,len;
			int cntr = 0;
			do {
				lat2 = 180.0 + (rng.nextDouble() * 2.0 - 1.0) * 60.0;
				lon2 = ridgeRng.nextDouble() * 360.0 - 180.0;
				len = Maths.gcDistance(lat, lon, lat2, lon2);
				if(++cntr == 1000) break;
			}while(len - targetLen > 0.01 || len - targetLen < 0 || Double.isNaN(len));
			if(cntr == 1000) continue;

			double iSize = Math.pow((len - ridgeMinlength) / (ridgeMaxlength - ridgeMinlength), 1.2);
			double size = iSize * (ridgeMaxsize - ridgeMinsize) + ridgeMinsize;
			double strength = iSize * (ridgeMaxstrength - ridgeMinstrength) + ridgeMinstrength;

			RavineConfig rconf = new RavineConfig();
			rconf.setDistortNoiseConfig(defaultRavineDistortNoise);
			rconf.setRavineStrength(strength);
			rconf.setShapeExponent(2.3 + Maths.biasedRNG(ridgeRng, 0.3));
			rconf.setRimWidth(0.15 + Maths.biasedRNG(ridgeRng, 0.025));
			rconf.setRimHeight(0.1 + Maths.biasedRNG(ridgeRng, 0.05));
			rconf.setRimNoise(defaultRavineRimNoise);
			rconf.setSize(size);
			gen.genRavine(temp2, temp, null, lat, lon, lat2, lon2, 0, height, false, rconf, ridgeRng);
		}
		ProgressBars.finishProgress();
		defaultRavineRimNoise.noise.cleanUp();
		gen = null;

		PostProcessingEffects.gaussianBlur(temp2, temp, 1.0 / (double)maria.length, 1.0 / (double)maria[0].length, (int)(16.0 * (double)width / 4096.0));
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			double d;
			if(temp[i][j] < 0.9) d = 0;
			else d = ((temp[i][j] - 0.9) / 0.1);
			d = Maths.biasFunction(Math.min(1, d), -0.6);
			maria[i][j] = Math.min(maria[i][j], d);
		}

		NoiseConfig nc = new NoiseConfig(new OctaveNoise3D(3, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(0.6).setNoiseScale(0.33).setDistortStrength(0).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, null, 1, true);

		nc = new NoiseConfig(new OctaveWorley(4, 2.0, 0.4)).setIsRidged(false).setNoiseStrength(1.0).setNoiseScale(0.4).setDistortStrength(0).setOffsetMap(temp).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp2, nc, null, 1, true);

		nc = new NoiseConfig(new OctaveNoise3D(4, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(1.1).setNoiseScale(0.6).setDistortStrength(0).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, null, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			double d = temp2[i][j] * temp[i][j];
			d = Math.max(0, Math.min(1, d * 1.0));
			d = 1 - d;
			if(d < 0.9) d = 0;
			else d = ((d - 0.9) / 0.1);
			maria[i][j] = Math.min(maria[i][j], d);
		}

		System.out.println("Ground (1)");
		nc = new NoiseConfig(new OctaveNoise3D(4, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(1.0).setNoiseScale(0.2).setDistortStrength(0.1).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, null, 1, true);

		nc = new NoiseConfig(new OctaveWorley(2, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(1.14).setNoiseScale(0.3).setDistortStrength(0).setNoiseOffset(-0.2).setOffsetMap(temp);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), cellsMap, nc, null, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			cellsMap[i][j] = Math.max(0, cellsMap[i][j]) / (1.0 - 0.2);

			finalHeightmap[i][j] = Math.max(0.1, maria[i][j] * 0.9) * cellsMap[i][j];

			finalHeightmap[i][j] += craterMinstrength * 2.0;
		}

		System.out.println("Craters"); //Aka code I shamelessly copy-pasted from Ralu's generator
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			craterDistrThingy[i][j] = maria[i][j] * 1.2 > 0.95;
			mariaCraterDistrThingy[i][j] = maria[i][j] * 1.2 < 0.1;
		}
		for(int i = 0; i < width; i++) Arrays.fill(temp[i], 0.75);
		FeatureDistributerConfig featureDistConf = new FeatureDistributerConfig(
				null, null,
				true, false,
				false,
				0, 0,
				new CraterConfig(0, 0, 0.1, 1.8, 1.5, 4.0, -0.9, 0.15, 1.5, 0.1, 2.5, 22, 600, 0.8),
				new CraterConfig(0, 0, 0.35, 0.8, 2.8, 3.2, -0.5, 0.4, 4.2, 0.12, 1.4, 22, 600, 0.8),
				null
		);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) temp3[i][j] = finalHeightmap[i][j];
		nc = new NoiseConfig(new OctaveNoise3D(24, 24, 24, 6, 2.0, 0.6)).setIsRidged(false).setNoiseStrength(2.1).setNoiseScale(0.2).setDistortStrength(0.6).setNoiseOffset(0.1);
		CraterDistributionSettings cds = new CraterDistributionSettings(craterCount, craterMinsize, craterMaxsize, craterMinstrength, craterMaxstrength, 4, 14, nc, 0.6);
		FeatureDistributer.distributeFeatures(featureDistConf, width, height, finalHeightmap, temp, craterMap, null, craterDistrThingy, null, cds, null, 1, new RanMT().seedCompletely(rng), true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			if(temp3[i][j] > 0 && craterMap[i][j] < 0) {
				double d = -craterMap[i][j];
				d = Maths.biasFunction(d, 0.2);
				d *= temp3[i][j] * 0.5;
				finalHeightmap[i][j] += d;
			}
		}

		System.out.println("Ground (2)");
		nc = new NoiseConfig(new OctaveNoise3D(4, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(0.04).setNoiseScale(0.2).setDistortStrength(0.8).setWiggleDensity(1.5).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp2, nc, maria, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] += temp2[i][j];

		System.out.println("Maria Craters");
		for(int i = 0; i < width; i++) Arrays.fill(temp[i], 0.75);
		featureDistConf = new FeatureDistributerConfig(
				null, null,
				true, false,
				false,
				0, 0,
				new CraterConfig(0, 0, 0.03, 1.5, 1.8, 3.6, -10, 0.0, 2.1, 0.1, 2.5, 200, 600, 1.0),
				null,
				null
		);
		cds = new CraterDistributionSettings(mariaCraterCount, mariaCraterMinsize, mariaCraterMaxsize, mariaCraterMinstrength, mariaCraterMaxstrength, 100, 200, null, 0.4);
		FeatureDistributer.distributeFeatures(featureDistConf, width, height, finalHeightmap, temp, temp3, null, mariaCraterDistrThingy, null, cds, null, 1, new RanMT().seedCompletely(rng), true);

		double min = 10000;
		double average = 0;
		double max = 0;
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			if(finalHeightmap[i][j] < min) min = finalHeightmap[i][j];
			average += finalHeightmap[i][j];
			if(finalHeightmap[i][j] > max) max = finalHeightmap[i][j];
		}
		average /= width * height;
		System.out.println("Min elevation: " + min);
		System.out.println("Max elevation: " + max);
		System.out.println("Avg elevation: " + average);

		System.out.println("Colors");
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		nc = new NoiseConfig(new OctaveWorley(6, 2.0, 0.75)).setIsRidged(false).setNoiseStrength(1.25).setNoiseScale(0.5).setDistortStrength(0.75).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, null, 1, true);

		final double[] mariaColor = MapUtils.RGB(new Color(51, 17, 3));
		final double[] baseColor = MapUtils.RGB(new Color(155, 85, 35));
		final double[] craterColor = MapUtils.RGB(new Color(32, 32, 32));
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			double[] rgb = new double[] {baseColor[0], baseColor[1], baseColor[2]};

			if(craterMap[i][j] < 0) {
				double mul = Math.min(1, -craterMap[i][j] / (craterMaxstrength * 0.8));
				MapUtils.factorInColor(rgb, mul * 0.85, craterColor);
			}
			double d = maria[i][j] + finalHeightmap[i][j];
			if(d < 0.15) {
				MapUtils.factorInColor(rgb, 1.0 - (d / 0.15), mariaColor);
			}
			double mul = temp[i][j] * 0.7 + 0.3;
			rgb[0] *= mul;
			rgb[1] *= mul;
			rgb[2] *= mul;

			if(craterMap[i][j] > craterRimFadeStart && temp2[i][j] > 0) {
				double mmul = (craterMap[i][j] - craterRimFadeStart) / (craterRimFadeEnd - craterRimFadeStart);
				mmul = Math.min(1, mmul);
				mmul *= temp2[i][j];
				rgb[0] *= 1.0 + craterRimFades[0] * mmul;
				rgb[1] *= 1.0 + craterRimFades[1] * mmul;
				rgb[2] *= 1.0 + craterRimFades[2] * mmul;
			}

			int r = (int)Math.max(0, Math.min(255, rgb[0] * 255.0));
			int g = (int)Math.max(0, Math.min(255, rgb[1] * 255.0));
			int b = (int)Math.max(0, Math.min(255, rgb[2] * 255.0));

			img.setRGB(i, j, b | (g << 8) | (r << 16));
		}
		ImageIO.write(MapUtils.render16bit(finalHeightmap), "png", new File("Tris_height.png"));
		ImageIO.write(img, "png", new File("Tris_colors.png"));

		BufferedImage scaledImg = new BufferedImage(4096, 2048, BufferedImage.TYPE_INT_RGB);
		double[][] scaledHm = new double[4096][2048];
		double offset = 500.0 / 9000.0;
		for(int i = 0; i < 4096; i++) for(int j = 0; j < 2048; j++) {
			int x = (int)((double)i / 4096.0 * width);
			int y = (int)((double)j / 2048.0 * height);
			scaledImg.setRGB(i, j, img.getRGB(x, y));
			scaledHm[i][j] = finalHeightmap[x][y];
		}
		DDSUtil.write(new File("Tris_scaled_colors.dds"), flipImageHorizontal(scaledImg), DDSImage.D3DFMT_DXT5, false);
		scaledImg = MapUtils.generateNormalMap(scaledHm, 176888, 6000, 1.0 / 3.0);
		for(int i = 0; i < scaledImg.getWidth(); i++) for(int j = 0; j < scaledImg.getHeight(); j++) {
			scaledImg.setRGB(i, j, scaledImg.getRGB(i, j) | 0x00FF00FF);
		}
		DDSUtil.write(new File("Tris_normals.dds"), flipImageHorizontal(scaledImg), DDSImage.D3DFMT_DXT5, false);

		System.out.println("Biomes");
		ProgressBars.printBar();
		for(int i = 0; i < width; i++) {
			ProgressBars.printProgress(i, width);
			for(int j = 0; j < height; j++) {
				double[] rgb = baseColor;
				if(maria[i][j] < 0.15) rgb = mariaColor;
				if(craterMap[i][j] < -0.08) rgb = new double[] {1, 1, 1};

				int r = (int)Math.max(0, Math.min(255, rgb[0] * 255.0));
				int g = (int)Math.max(0, Math.min(255, rgb[1] * 255.0));
				int b = (int)Math.max(0, Math.min(255, rgb[2] * 255.0));

				img.setRGB(i, j, b | (g << 8) | (r << 16));
			}
		}
		ProgressBars.finishProgress();
		ImageIO.write(img, "png", new File("Tris_biomes.png"));
	}
	
	public static void genKalt(int w, long seed) throws Exception {
		final int width = w;
		final int height = w / 2;
		
		RanMT rng = new RanMT().seedCompletely(new Random(seed));
		
		double[][] cellsMap = new double[width][height];
		double[][] temp = new double[width][height];
		double[][] temp2 = new double[width][height];
		double[][] maria = new double[width][height];
		double[][] mountains = new double[width][height];
		double[][] mountainMap = new double[width][height];
		double[][] craterMap = new double[width][height];
		double[][] finalHeightmap = new double[width][height];

		final double craterMaxsize = 64;
		final double craterMinsize = 8;
		final double craterMaxstrength = 0.30;
		final double craterMinstrength = 0.04;
		final int    craterCount = 256;

		System.out.println("Cells");
		NoiseConfig nc = new NoiseConfig(new OctaveNoise3D(4, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(0.77).setNoiseScale(0.3).setDistortStrength(0.1).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, null, 1, true);

		nc = new NoiseConfig(new OctaveWorley(2, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(1.0).setNoiseScale(0.2).setDistortStrength(0).setNoiseOffset(-0.4).setOffsetMap(temp);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), cellsMap, nc, null, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			cellsMap[i][j] = Math.max(0, cellsMap[i][j]) / (1.0 - 0.4);
			cellsMap[i][j] = Maths.biasFunction(Math.min(1, cellsMap[i][j] * 1.2), 0.4);
		}

		System.out.println("Biomes");
		nc = new NoiseConfig(new OctaveNoise3D(4, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(3.0).setNoiseScale(0.8).setDistortStrength(0.1).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), maria, nc, null, 1, true);
		nc = new NoiseConfig(new OctaveNoise3D(4, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(6.0).setNoiseScale(0.3).setDistortStrength(0.3).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, null, 1, true);
		final double mountainThreshold = 0.2;
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			double d = maria[i][j];
			if(d > 0 && d < mountainThreshold) {
				if(d < mountainThreshold * 0.5) mountainMap[i][j] = d / (mountainThreshold * 0.5);
				else mountainMap[i][j] = 1.0 - ((d - (mountainThreshold * 0.5)) / (mountainThreshold * 0.5));
				mountainMap[i][j] *= Math.max(0, Math.min(1, temp[i][j]));
				mountainMap[i][j] = Maths.biasFunction(mountainMap[i][j], 0.1);
			}
			maria[i][j] = Math.max(0, Math.min(1, d));
		}

		System.out.println("Mountains");
		nc = new NoiseConfig(new OctaveNoise3D(12, 2.0, 0.5)).setIsRidged(true).setNoiseStrength(2.0).setNoiseScale(0.05).setDistortStrength(0.6).setWiggleDensity(2.0).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), mountains, nc, mountainMap, 1, true);

		System.out.println("Ground (1)");
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) temp[i][j] = (1 - Math.min(1, Math.max(maria[i][j] * 1.25, mountainMap[i][j]) * 1.5)) * ((1 - cellsMap[i][j]) * 0.7 + 0.3);
		nc = new NoiseConfig(new OctaveWorley(3, 2.0, 0.5)).setNoiseStrength(0.25).setNoiseScale(0.1).setDistortStrength(0.1).setNoiseOffset(-0.2);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), finalHeightmap, nc, temp, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] = Math.max(0, finalHeightmap[i][j]);

		//Add cells
		final double cellStrength = 0.2;
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			finalHeightmap[i][j] += cellStrength;
			finalHeightmap[i][j] -= cellsMap[i][j] * cellStrength;
		}

		System.out.println("Craters");
		boolean[][] craterDistrMap = new boolean[width][height];
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) craterDistrMap[i][j] = mountainMap[i][j] < 0.05 && cellsMap[i][j] < 0.25 && maria[i][j] < 0.1;
		for(int i = 0; i < width; i++) {
			Arrays.fill(temp[i], 0.75);
			Arrays.fill(craterMap[i], 0);
		}
		FeatureDistributerConfig featureDistConf = new FeatureDistributerConfig(
				null, null,
				true, false,
				false,
				0, 0,
				new CraterConfig(0, 0, 0.3, 0.9, 2.8, 3.2, -0.75, 0.45, 2.0, 0.2, 1.5, 600, 600, 0.8),
				null,
				null
		);
		nc = new NoiseConfig(new OctaveNoise3D(24, 24, 24, 6, 2.0, 0.6)).setIsRidged(false).setNoiseStrength(2.1).setNoiseScale(0.2).setDistortStrength(0.7).setNoiseOffset(0.1);
		CraterDistributionSettings cds = new CraterDistributionSettings(craterCount, craterMinsize, craterMaxsize, craterMinstrength, craterMaxstrength, 4000, 14000, nc, 0.5);
		FeatureDistributer.distributeFeatures(featureDistConf, width, height, finalHeightmap, temp, craterMap, null, craterDistrMap, null, cds, null, 1, new RanMT().seedCompletely(rng), true);

		System.out.println("Ground (2)");
		nc = new NoiseConfig(new OctaveNoise3D(3, 2.0, 0.5)).setNoiseStrength(0.02).setNoiseScale(0.035).setDistortStrength(0).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp2, nc, temp, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] += Math.max(0, temp2[i][j]);

		nc = new NoiseConfig(new OctaveNoise3D(3, 2.0, 0.5)).setNoiseStrength(0.07777).setNoiseScale(0.1).setDistortStrength(0).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp2, nc, maria, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] += Math.max(0, temp2[i][j]);

		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] += mountains[i][j];

		double min = 10000;
		double average = 0;
		double max = 0;
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			if(finalHeightmap[i][j] < min) min = finalHeightmap[i][j];
			average += finalHeightmap[i][j];
			if(finalHeightmap[i][j] > max) max = finalHeightmap[i][j];
		}
		average /= width * height;
		System.out.println("Min elevation: " + min);
		System.out.println("Max elevation: " + max);
		System.out.println("Avg elevation: " + average);

		System.out.println("Colors");
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		nc = new NoiseConfig(new OctaveWorley(6, 2.0, 0.75)).setIsRidged(false).setNoiseStrength(1.25).setNoiseScale(0.5).setDistortStrength(0.5).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, null, 1, true);
		nc = new NoiseConfig(new OctaveNoise3D(4, 2.0, 0.75)).setIsRidged(false).setNoiseStrength(3.1).setNoiseScale(0.35).setDistortStrength(0.75).setNoiseOffset(0.02);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp2, nc, null, 1, true);

		final double[] baseColor = MapUtils.RGB(new Color(222, 222, 222));
		final double[] mariaColor = MapUtils.RGB(new Color(120, 120, 120));
		final double[] secondaryColor = MapUtils.RGB(new Color(255, 222, 190));
		final double[] mountainsColor = MapUtils.RGB(new Color(66, 12, 18));
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			double[] rgb = new double[] {baseColor[0], baseColor[1], baseColor[2]};

			if(temp2[i][j] > 0) {
				double mul = Math.min(1, temp2[i][j]);
				rgb[0] = rgb[0] * (1.0 - mul) + secondaryColor[0] * mul;
				rgb[1] = rgb[1] * (1.0 - mul) + secondaryColor[1] * mul;
				rgb[2] = rgb[2] * (1.0 - mul) + secondaryColor[2] * mul;
			}

			if(mountainMap[i][j] > 0.05) {
				double mul = Math.min(1, (mountainMap[i][j] - 0.05) / 0.1);
				mul *= 1 - Math.min(1, mountains[i][j] / 0.15);
				rgb[0] = rgb[0] * (1.0 - mul) + mountainsColor[0] * mul;
				rgb[1] = rgb[1] * (1.0 - mul) + mountainsColor[1] * mul;
				rgb[2] = rgb[2] * (1.0 - mul) + mountainsColor[2] * mul;
			}

			if(maria[i][j] > 0) {
				double mul = Math.min(1, maria[i][j] * 3.0);
				rgb[0] = rgb[0] * (1.0 - mul) + mariaColor[0] * mul;
				rgb[1] = rgb[1] * (1.0 - mul) + mariaColor[1] * mul;
				rgb[2] = rgb[2] * (1.0 - mul) + mariaColor[2] * mul;
			}

			double mul = 1 - Math.max(0, Math.min(1, cellsMap[i][j]));
			mul *= 0.15;
			mul += 0.85;
			rgb[0] *= mul;
			rgb[1] *= mul;
			rgb[2] *= mul;

			mul = temp[i][j] * 0.4 + 0.6;
			rgb[0] *= mul;
			rgb[1] *= mul;
			rgb[2] *= mul;

			int r = (int)Math.max(0, Math.min(255, rgb[0] * 255.0));
			int g = (int)Math.max(0, Math.min(255, rgb[1] * 255.0));
			int b = (int)Math.max(0, Math.min(255, rgb[2] * 255.0));

			img.setRGB(i, j, b | (g << 8) | (r << 16));
		}
		ImageIO.write(img, "png", new File("Kalt_colors.png"));
		ImageIO.write(MapUtils.render16bit(finalHeightmap), "png", new File("Kalt_height.png"));

		BufferedImage scaledImg = new BufferedImage(4096, 2048, BufferedImage.TYPE_INT_RGB);
		double[][] scaledHm = new double[4096][2048];
		double offset = 500.0 / 9000.0;
		for(int i = 0; i < 4096; i++) for(int j = 0; j < 2048; j++) {
			int x = (int)((double)i / 4096.0 * width);
			int y = (int)((double)j / 2048.0 * height);
			scaledImg.setRGB(i, j, img.getRGB(x, y));
			scaledHm[i][j] = finalHeightmap[x][y];
		}
		DDSUtil.write(new File("Kalt_scaled_colors.dds"), flipImageHorizontal(scaledImg), DDSImage.D3DFMT_DXT5, false);
		scaledImg = MapUtils.generateNormalMap(scaledHm, 224778, 8000, 1.0 / 2.5);
		for(int i = 0; i < scaledImg.getWidth(); i++) for(int j = 0; j < scaledImg.getHeight(); j++) {
			scaledImg.setRGB(i, j, scaledImg.getRGB(i, j) | 0x00FF00FF);
		}
		DDSUtil.write(new File("Kalt_normals.dds"), flipImageHorizontal(scaledImg), DDSImage.D3DFMT_DXT5, false);

		System.out.println("Biomes");
		ProgressBars.printBar();
		for(int i = 0; i < width; i++) {
			ProgressBars.printProgress(i, width);
			for(int j = 0; j < height; j++) {
				double[] rgb = baseColor;
				if(mountainMap[i][j] > 0.1) rgb = mountainsColor;
				if(maria[i][j] > 0.1) rgb = mariaColor;
				if(craterMap[i][j] < -0.08) rgb = new double[] {1, 1, 1};

				int r = (int)Math.max(0, Math.min(255, rgb[0] * 255.0));
				int g = (int)Math.max(0, Math.min(255, rgb[1] * 255.0));
				int b = (int)Math.max(0, Math.min(255, rgb[2] * 255.0));

				img.setRGB(i, j, b | (g << 8) | (r << 16));
			}
		}
		ProgressBars.finishProgress();
		ImageIO.write(img, "png", new File("Kalt_biomes.png"));
	}
	
	public static void genNoir(int w, long seed) throws Exception {
		final int width = w;
		final int height = w / 2;
		
		final double ridgeMaxlength = Math.PI / 4.0 - 0.1;
		final double ridgeMinlength = Math.PI / 45.0;
		final double ridgeMaxsize = 8;
		final double ridgeMinsize = 2;
		final double ridgeMaxstrength = 0.5;
		final double ridgeMinstrength = 0.1;
		final int ridgeCount = 50;
		
		final double craterMaxsize = 64;
		final double craterMinsize = 16;
		final double craterMaxstrength = 0.45;
		final double craterMinstrength = 0.12;
		final int    craterCount = 196;
		
		visualizeCraterSizes(8000, 328700, craterMaxsize, craterMinsize, craterMaxstrength, craterMinstrength, 2.8, 3.2);
		
		double[][] ridgeMap = new double[width][height];
		double[][] temp = new double[width][height];
		double[][] temp2 = new double[width][height];
		double[][] mountainMap = new double[width][height];
		double[][] mulsMap = new double[width][height];
		double[][] seasMap = new double[width][height];
		double[][] craterMap = new double[width][height];
		double[][] continentsMap = new double[width][height];
		double[][] finalHeightmap = new double[width][height];
		for(int i = 0; i < width; i++) Arrays.fill(ridgeMap[i], 0.5);
		
		RanMT rng = new RanMT().seedCompletely(new Random(seed));
		
		System.out.println("Ridges");
		RanMT ridgeRng = new RanMT().seedCompletely(rng);
		NoiseConfig defaultRavineDistortNoise = new NoiseConfig(new OctaveNoise2D(6, 6, 8, 1.6, 0.6), false, 10, 20, 0.222, 3.7, 0, 0);
		NoiseConfig defaultRavineRimNoise = new NoiseConfig(new OctaveNoise3D(24, 24, 24, 10, 2.0, 0.65)).setIsRidged(true).setNoiseStrength(1.0 / 0.23).setNoiseScale(0.72).setDistortStrength(0.43).setNoiseOffset(0);
		defaultRavineRimNoise.noise.initialize(new RanMT().seedCompletely(rng));
		RavineGenerator gen = new RavineGenerator(width, height);
		ProgressBars.printBar();
		for(int i = 0; i < width; i++) Arrays.fill(temp[i], 0.75);
		for(int i = 0; i < ridgeCount; i++) {
			ProgressBars.printProgress(i, ridgeCount - 1);
			double targetLen = Maths.biasFunction(ridgeRng.nextDouble(), 0.3) * (ridgeMaxlength - ridgeMinlength - 0.005) + ridgeMinlength;
			
			double lat = 180.0 + (rng.nextDouble() * 2.0 - 1.0) * 60.0;
			double lon = ridgeRng.nextDouble() * 340.0 + 10.0 - 180.0;
			double lat2,lon2,len;
			int cntr = 0;
			do {
				lat2 = 180.0 + (rng.nextDouble() * 2.0 - 1.0) * 60.0;
				lon2 = ridgeRng.nextDouble() * 360.0 - 180.0;
				len = Maths.gcDistance(lat, lon, lat2, lon2);
				if(++cntr == 1000) break;
			}while(len - targetLen > 0.01 || len - targetLen < 0 || Double.isNaN(len));
			if(cntr == 1000) continue;
			
			double iSize = Math.pow((len - ridgeMinlength) / (ridgeMaxlength - ridgeMinlength), 1.2);
			double size = iSize * (ridgeMaxsize - ridgeMinsize) + ridgeMinsize;
			double strength = iSize * (ridgeMaxstrength - ridgeMinstrength) + ridgeMinstrength;
			
			RavineConfig rconf = new RavineConfig();
			rconf.setDistortNoiseConfig(defaultRavineDistortNoise);
			rconf.setRavineStrength(strength);
			rconf.setShapeExponent(2.3 + Maths.biasedRNG(ridgeRng, 0.3));
			rconf.setRimWidth(0.15 + Maths.biasedRNG(ridgeRng, 0.025));
			rconf.setRimHeight(0.1 + Maths.biasedRNG(ridgeRng, 0.05));
			rconf.setRimNoise(defaultRavineRimNoise);
			rconf.setSize(size);
			gen.genRavine(ridgeMap, temp, null, lat, lon, lat2, lon2, 0, height, false, rconf, ridgeRng);
		}
		ProgressBars.finishProgress();
		defaultRavineRimNoise.noise.cleanUp();
		gen = null;
		
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			ridgeMap[i][j] = Math.min(0.5, ridgeMap[i][j]);
			ridgeMap[i][j] = -(ridgeMap[i][j] - 0.5) * 2.222;
		}
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) temp[i][j] = ridgeMap[i][j];
		PostProcessingEffects.gaussianBlur(temp, ridgeMap, 1.0 / (double)ridgeMap.length, 1.0 / (double)ridgeMap[0].length, (int)(16.0 * (double)width / 4096.0));
		
		System.out.println("Mountains");
		NoiseConfig nc = new NoiseConfig(new OctaveNoise3D(4, 2.0, 0.6)).setIsRidged(false).setNoiseStrength(1.8).setNoiseScale(0.12).setDistortStrength(0.1).setNoiseOffset(0.1);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), finalHeightmap, nc, null, 1, true);
		
		nc = new NoiseConfig(new OctaveNoise3D(24, 24, 24, 6, 2.0, 0.65)).setIsRidged(true).setNoiseStrength(1.88).setNoiseScale(0.5).setDistortStrength(0.3).setNoiseOffset(0.2).setOffsetMap(finalHeightmap);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), mountainMap, nc, ridgeMap, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] = mountainMap[i][j];
		
		System.out.println("Seas");
		nc = new NoiseConfig(new OctaveNoise3D(3, 2.0, 0.5)).setNoiseStrength(0.555).setNoiseScale(0.22).setDistortStrength(0.1).setWiggleDensity(3.7);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), seasMap, nc, null, 1, true);
		
		nc = new NoiseConfig(new OctaveNoise3D(5, 2.0, 0.4)).setNoiseStrength(12).setNoiseScale(1.0).setDistortStrength(0.3).setOffsetMap(seasMap).setNoiseOffset(0.16).setZOffset(5);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), seasMap, nc, null, 1, true);
		
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			continentsMap[i][j] = Math.min(1, Math.max(0, seasMap[i][j]));
			if(continentsMap[i][j] < 1) {
				finalHeightmap[i][j] = Math.max(0, finalHeightmap[i][j] - (1 - continentsMap[i][j]) * 0.05);
			}
			seasMap[i][j] = Math.min(0, Math.max(-1, seasMap[i][j] + finalHeightmap[i][j])) + 1.0;
			
			mulsMap[i][j] = continentsMap[i][j] * (1 - finalHeightmap[i][j]);
		}
		
		System.out.println("Ground");
		nc = new NoiseConfig(new OctaveNoise3D(24, 24, 24, 8, 2.0, 0.5)).setIsRidged(false).setNoiseStrength(0.67).setNoiseScale(0.09).setDistortStrength(0.3).setNoiseOffset(0.2).setWiggleDensity(3.2);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp2, nc, mulsMap, 1, true);
		nc = new NoiseConfig(new OctaveNoise3D(2, 2.0, 0.5)).setNoiseStrength(0.08).setNoiseScale(0.03).setDistortStrength(0.1).setWiggleDensity(2.0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, mulsMap, 1, true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) temp2[i][j] += temp[i][j];
		
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] += temp2[i][j] * 0.85;
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] = Math.max(0, finalHeightmap[i][j]);
		
		System.out.println("Craters");
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] += 0.15;
		boolean[][] craterDistrMap = new boolean[width][height];
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) craterDistrMap[i][j] = mulsMap[i][j] > 0.95;
		for(int i = 0; i < width; i++) Arrays.fill(temp[i], 0.75);
		FeatureDistributerConfig featureDistConf = new FeatureDistributerConfig(
				null, null,
				true, false,
				false,
				0, 0,
				new CraterConfig(0, 0, 0.3, 0.9, 2.8, 3.2, -10, 0.45, 2.0, 0.2, 1.5, 24, 600, 0.8),
				null,
				null
		);
		nc = new NoiseConfig(new OctaveNoise3D(24, 24, 24, 6, 2.0, 0.6)).setIsRidged(false).setNoiseStrength(2.1).setNoiseScale(0.2).setDistortStrength(0.7).setNoiseOffset(0.1);
		CraterDistributionSettings cds = new CraterDistributionSettings(craterCount, craterMinsize, craterMaxsize, craterMinstrength, craterMaxstrength, 4000, 14000, nc, 0.3);
		FeatureDistributer.distributeFeatures(featureDistConf, width, height, finalHeightmap, temp, craterMap, null, craterDistrMap, null, cds, null, 1, new RanMT().seedCompletely(rng), true);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] -= 0.15;
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) finalHeightmap[i][j] += temp2[i][j] * 0.15;
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			if(finalHeightmap[i][j] < 0) {
				seasMap[i][j] += finalHeightmap[i][j] * (9000.0 / 500.0);
				finalHeightmap[i][j] = Math.max(0, finalHeightmap[i][j]);
			}
		}
		
		double min = 10000;
		double average = 0;
		double max = 0;
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			if(finalHeightmap[i][j] < min) min = finalHeightmap[i][j];
			average += finalHeightmap[i][j];
			if(finalHeightmap[i][j] > max) max = finalHeightmap[i][j];
		}
		average /= width * height;
		System.out.println("Min elevation: " + min);
		System.out.println("Max elevation: " + max);
		System.out.println("Avg elevation: " + average);
		
		ImageIO.write(MapUtils.render16bit(finalHeightmap), "png", new File("Noir_height.png"));
		ImageIO.write(MapUtils.render16bit(seasMap), "png", new File("Noir_oceans.png"));
		
		System.out.println("Colors");
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		nc = new NoiseConfig(new OctaveWorley(4, 2.0, 0.75)).setIsRidged(false).setNoiseStrength(1.25).setNoiseScale(0.5).setDistortStrength(0.75).setNoiseOffset(0);
		NoisemapGenerator.genNoisemap(new RanMT().seedCompletely(rng), temp, nc, null, 1, true);
		
		final double[] baseColor = MapUtils.RGB(new Color(48, 48, 48));
		final double[] ridgesColor = MapUtils.RGB(new Color(100, 100, 100));
		final double[] craterInsideColor = MapUtils.RGB(new Color(255, 255, 255));
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) {
			double[] rgb = new double[] {baseColor[0], baseColor[1], baseColor[2]};
			
			double mul = Math.min(1, Maths.biasFunction(Math.min(1, Math.max(0, ridgeMap[i][j] * 1.2 - 0.1)), -0.4) * 1.32);
			if(mountainMap[i][j] < 0.1) {
				mul *= (mountainMap[i][j] / 0.1) * 0.75 + 0.25;
			}
			if(mul > 1e-3) {
				rgb[0] = rgb[0] * (1.0 - mul) + ridgesColor[0] * mul;
				rgb[1] = rgb[1] * (1.0 - mul) + ridgesColor[1] * mul;
				rgb[2] = rgb[2] * (1.0 - mul) + ridgesColor[2] * mul;
			}
			
			if(craterMap[i][j] < 0) {
				mul = Math.min(1, craterMap[i][j] / -0.1);
				
				rgb[0] = rgb[0] * (1.0 - mul) + craterInsideColor[0] * mul;
				rgb[1] = rgb[1] * (1.0 - mul) + craterInsideColor[1] * mul;
				rgb[2] = rgb[2] * (1.0 - mul) + craterInsideColor[2] * mul;
			}
			
			mul = temp[i][j] * 0.5;
			rgb[0] *= mul;
			rgb[1] *= mul;
			rgb[2] *= mul;
			
			int r = (int)Math.max(0, Math.min(255, rgb[0] * 255.0));
			int g = (int)Math.max(0, Math.min(255, rgb[1] * 255.0));
			int b = (int)Math.max(0, Math.min(255, rgb[2] * 255.0));
			
			img.setRGB(i, j, b | (g << 8) | (r << 16));
		}
		ImageIO.write(img, "png", new File("Noir_colors.png"));
		
		BufferedImage scaledImg = new BufferedImage(4096, 2048, BufferedImage.TYPE_INT_ARGB);
		double[][] scaledHm = new double[4096][2048];
		double offset = 500.0 / 8000.0;
		for(int i = 0; i < 4096; i++) for(int j = 0; j < 2048; j++) {
			int x = (int)((double)i / 4096.0 * width);
			int y = (int)((double)j / 2048.0 * height);
			if(seasMap[x][y] * offset - offset + finalHeightmap[x][y] < 0) {
				scaledImg.setRGB(i, j, 0xFF000000);
				scaledHm[i][j] = 0;
			}
			else {
				scaledImg.setRGB(i, j, img.getRGB(x, y) & 0x00FFFFFF);
				scaledHm[i][j] = finalHeightmap[x][y];
			}
		}
		DDSUtil.write(new File("Noir_scaled_colors.dds"), flipImageHorizontal(scaledImg), DDSImage.D3DFMT_DXT5, false);
		scaledImg = MapUtils.generateNormalMap(scaledHm, 328700, 8000, 1.0 / 2.5);
		for(int i = 0; i < scaledImg.getWidth(); i++) for(int j = 0; j < scaledImg.getHeight(); j++) {
			scaledImg.setRGB(i, j, scaledImg.getRGB(i, j) | 0x00FF00FF);
		}
		DDSUtil.write(new File("Noir_normals.dds"), flipImageHorizontal(scaledImg), DDSImage.D3DFMT_DXT5, false);
		
		System.out.println("Biome Map");
		ProgressBars.printBar();
		for(int i = 0; i < width; i++) {
			ProgressBars.printProgress(i, width);
			for(int j = 0; j < height; j++) {
				if(seasMap[i][j] * offset - offset + finalHeightmap[i][j] < 0) {
					img.setRGB(i, j, 0);
					continue;
				}
				
				double[] rgb = baseColor;
				if(ridgeMap[i][j] > 0.15) rgb = ridgesColor;
				if(craterMap[i][j] < -0.15) rgb = craterInsideColor;
				
				int r = (int)Math.max(0, Math.min(255, rgb[0] * 255.0));
				int g = (int)Math.max(0, Math.min(255, rgb[1] * 255.0));
				int b = (int)Math.max(0, Math.min(255, rgb[2] * 255.0));
				
				img.setRGB(i, j, b | (g << 8) | (r << 16));
			}
		}
		ProgressBars.finishProgress();
		ImageIO.write(img, "png", new File("Noir_biomes.png"));
	}
	
	public static void visualizeCraterSizes(double deformity, double radius, double maxsize, double minsize, double maxstrength, double minstrength, double p1, double p2) {
		double circ = radius * 2.0 * Math.PI;
		CraterGenerator gen = new CraterGenerator(4096, 2048);
		double[][] map = new double[4096][2048];
		for(int i = 0; i < 4096; i++) Arrays.fill(map[i], 0.75);
		gen.genCrater(map, null, map, null, 0, 2048, 0, -12, new CraterConfig(minsize, minstrength, 0, 1.0, p1, p2, -10.0, 0, 0, 0, 1.0, 10000.0, 100000.0, 1.0), null, new Random());
		gen.genCrater(map, null, map, null, 0, 2048, 0, 12, new CraterConfig(maxsize, maxstrength, 0, 1.0, p1, p2, -10.0, 0, 0, 0, 1.0, 10000.0, 100000.0, 1.0), null, new Random());
		int height = (int)(deformity / 10.0);
		int width = (int)((circ / 4096.0) * 512.0 / 10.0);
		BufferedImage crossSection = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)crossSection.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, crossSection.getWidth(), crossSection.getHeight());
		g.setColor(Color.WHITE);
		for(int i = 0; i < width; i++) {
			int pix = (int)((double)i / (double)width * 512);
			double h = map[2048 - 256 + pix][1024];
			int h2 = (int)(h * (deformity / 10.0));
			g.drawLine(i, height - 1, i, height - h2 - 1);
		}
		g.setColor(Color.RED);
		g.drawLine(0, (int)(0.25 * height), width, (int)(0.25 * height));
		try {
			ImageIO.write(crossSection, "png", new File("craters.png"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage flipImageHorizontal(BufferedImage img) {
		int[] colBuff = new int[img.getHeight()];
		int w = img.getWidth();
		int h = img.getHeight();
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) colBuff[j] = img.getRGB(i, j);
			for(int j = 0; j < h; j++) img.setRGB(i, h - 1 - j, colBuff[j]);
		}
		return img;
	}
}
