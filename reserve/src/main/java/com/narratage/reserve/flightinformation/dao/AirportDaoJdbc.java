package com.narratage.reserve.flightinformation.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.narratage.reserve.flightinformation.domain.Airport;

public class AirportDaoJdbc implements AirportDao {
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private RowMapper<Airport> airportMapper = new RowMapper<Airport>() {
		public Airport mapRow(ResultSet rs, int rowNum) throws SQLException {
			Airport airport = new Airport();
			airport.setAltitude(rs.getInt("Altitude"));
			airport.setCity(rs.getString("City"));
			airport.setContry(rs.getString("Contry"));
			airport.setDst(rs.getString("DST"));
			airport.setIata(rs.getString("IATA"));
			airport.setLatitude(rs.getDouble("Latitude"));
			airport.setLongtitude(rs.getDouble("Longtitude"));
			airport.setQueriedNumber(rs.getInt("Queried_Number"));
			airport.setTimezosne(rs.getFloat("Timezone"));
			return airport;
		}
	};

	public Airport getAirportInfo(String IATA) {
		return this.jdbcTemplate.queryForObject("select * from airport where IATA = ?", new Object[] { IATA },
				this.airportMapper);
	}

	public void updatePlusOneAtQueriedNumber(String IATA) {
		System.out.println("IATA : "+IATA);
		this.jdbcTemplate.update("UPDATE AIRPORT SET QUERIED_NUMBER=QUERIED_NUMBER+1");
	}

	public List<Airport> getAirports4Map(double topLeftLat, double topLeftLong, double botRightLat,
			double botRightLong, int MaximumCitiesNumber) {
		return null;
	}

}
