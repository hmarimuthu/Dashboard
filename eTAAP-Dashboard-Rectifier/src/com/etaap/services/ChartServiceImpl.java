package com.etaap.services;

import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etaap.beans.ChartModel;
import com.etaap.dao.ChartsDao;
import com.etaap.utils.ChartConfigManager;
import com.etaap.utils.ColumnChartUtility;
import com.etaap.utils.ColumnSecondaryCategoryChartUtility;
import com.etaap.utils.MultiLineChartUtility;
import com.etaap.utils.PieChartUtility;
import com.etaap.utils.StackedChartUtility;
import com.etaap.utils.WaterTankChartUtility;

@Service("chartService")
public class ChartServiceImpl implements ChartService {

	@Autowired
	public ChartsDao chartsDao;

	private static final Logger logger = Logger
			.getLogger(ChartServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public String getCharts(ServletContext servletContext,
			List<ChartModel> chartsList) {

		ChartConfigManager chartConfigManager = new ChartConfigManager(
				servletContext);
		JSONObject chartJson;
		JSONArray charts = new JSONArray();
		for (ChartModel chartModel : chartsList) {
			chartJson = chartConfigManager.getChartInfo(chartModel
					.getChartName());

			switch (chartModel.getChartType()) {
			case NORMAL_PIE:
				try {
					charts.add(PieChartUtility.generateChartJSON(chartsDao
							.fetchChartData(
									(String) chartJson.get("aggregateQuery"),
									chartModel.getAggregateQueryObj()),
							chartsDao.fetchChartData((String) chartJson
									.get("secondAggregateQuery"), chartModel
									.getAggregateQueryObj()), null, chartJson));
				} catch (Exception e) {
					logger.error("ERROR :: getCharts() :: NORMAL_PIE :: "
							+ e.getMessage());
				}

				break;

			case DRILLDOWN_PIE:
				try {
					charts.add(PieChartUtility.generateChartJSON(chartsDao
							.fetchChartData(
									(String) chartJson.get("aggregateQuery"),
									chartModel.getAggregateQueryObj()),
							chartsDao.fetchChartData((String) chartJson
									.get("secondAggregateQuery"), chartModel
									.getAggregateQueryObj()), chartsDao
									.fetchChartData((String) chartJson
											.get("drillDownQuery"), chartModel
											.getDrillDownQueryObj()), chartJson));
				} catch (Exception e) {
					logger.error("ERROR :: getCharts() :: DRILLDOWN_PIE :: "
							+ e.getMessage());
				}

				break;

			case STACKED_COLUMN:
				try {
					charts.add(StackedChartUtility.generateChartJSON(chartsDao
							.fetchChartData(
									(String) chartJson.get("aggregateQuery"),
									chartModel.getAggregateQueryObj()),
							chartsDao.fetchChartData((String) chartJson
									.get("secondAggregateQuery"), chartModel
									.getSecondAggrQueryObj()), chartJson));
				} catch (Exception e) {
					logger.error("ERROR :: getCharts() :: STACKED_COLUMN :: "
							+ e.getMessage());
				}

				break;

			case MULTILINE_STACKEDBAR_FIXED_CATEGORY:
				try {
					charts.add(MultiLineChartUtility.generateChartJSON(
							chartModel.getChartCategories(), chartsDao
									.fetchChartData((String) chartJson
											.get("aggregateQuery"), chartModel
											.getAggregateQueryObj()),
							chartsDao.fetchChartData((String) chartJson
									.get("secondAggregateQuery"), chartModel
									.getSecondAggrQueryObj()), chartJson));
				} catch (Exception e) {
					logger.error("ERROR :: getCharts() :: MULTILINE_STACKEDBAR_FIXED_CATEGORY :: "
							+ e.getMessage());
				}

				break;

			case COLUMN:
				try {
					charts.add(ColumnChartUtility.generateChartJSON(chartModel
							.getChartCategories(), chartsDao.fetchChartData(
							(String) chartJson.get("aggregateQuery"),
							chartModel.getAggregateQueryObj()), chartsDao
							.fetchChartData((String) chartJson
									.get("secondAggregateQuery"), chartModel
									.getSecondAggrQueryObj()), chartJson));
				} catch (Exception e) {
					logger.error("ERROR :: getCharts() :: COLUMN :: "
							+ e.getMessage());
				}

				break;

			case COLUMN_SECONDARY_CATEGORY:
				try {
					charts.add(ColumnSecondaryCategoryChartUtility.generateChartJSON(
							chartModel.getChartCategories(), chartsDao
									.fetchChartData((String) chartJson
											.get("aggregateQuery"), chartModel
											.getAggregateQueryObj()),
							chartsDao.fetchChartData((String) chartJson
									.get("secondAggregateQuery"), chartModel
									.getSecondAggrQueryObj()), chartJson));
				} catch (Exception e) {
					logger.error("ERROR :: getCharts() :: COLUMN_SECONDARY_CATEGORY :: "
							+ e.getMessage());
				}

				break;

			case WATER_TANK:
				try {
					charts.add(WaterTankChartUtility.generateChartJSON(
							chartsDao.fetchChartData(
									(String) chartJson.get("totalCountQuery"),
									chartModel.getAggregateQueryObj()),
							chartsDao.fetchChartData(
									(String) chartJson.get("statusCountQuery"),
									chartModel.getDrillDownQueryObj()),
							chartJson));
				} catch (Exception e) {
					logger.error("ERROR :: getCharts() :: WATER_TANK :: "
							+ e.getMessage());
				}

				break;

			default:
				break;
			}
		}

		/*
		 * JSONObject sevChart = ddp.generateDrillDownChart(
		 * chartsService.readData((String)
		 * appSeverityDrillDown.get("aggregateQuery")),
		 * chartsService.readData((String)
		 * appSeverityDrillDown.get("drillDownQuery")),
		 * ChartType.DRILLDOWN_PIE,appSeverityDrillDown);
		 */

		System.out.println("final json created is >>" + charts.toJSONString());

		return charts.toJSONString();
	}
}
