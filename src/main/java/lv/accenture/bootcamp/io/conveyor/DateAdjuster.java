package lv.accenture.bootcamp.io.conveyor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lv.accenture.bootcamp.db.DBUtil;
import lv.accenture.bootcamp.network.SunAPIService;

@Component
public class DateAdjuster {
	
	@Autowired
	
	SunAPIService sunAPIService = null;

	public void adjustLectionTime() {
		
		Connection connection = null;	
		SunAPIService sunAPISerrvice = null;
		
		try {
			Path path = Paths.get("./documents/course-id.txt");
			List<String> fileLines = Files.readAllLines(path);
			String courseIdStr = fileLines.get(0);
			Long courseId = Long.parseLong(courseIdStr);
			System.out.println("course Id = "+courseId);
			
			
			connection = DBUtil.acquireConnection();
			String selectLectionSql = "select ID, LECTION_DTM from LECTION where COURSE_ID = ?";
			PreparedStatement lectionSelectStatement = connection.prepareStatement(selectLectionSql);
			lectionSelectStatement.setLong(1, courseId);
			ResultSet lectionResultSet= lectionSelectStatement.executeQuery();
			while(lectionResultSet.next()){
				Long lectionId = lectionResultSet.getLong("ID");
				java.sql.Date lectionDate =lectionResultSet.getDate("LECTION_DTM");
				long lectionDateRaw = lectionDate.getTime();
				System.out.println("row = "+ lectionId+  " date msec : " + lectionDateRaw);
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
				String lectionDateFormatted= simpleDateFormat.format(new java.util.Date(lectionDateRaw));
				java.util.Date sunriseDate= sunAPIService.getSunrise(lectionDateFormatted);
				
				System.out.println("Date : " + lectionDateFormatted + " | sunsire : " + sunriseDate);
				long sunriseTimeRaw = sunriseDate.getTime();
				
				
				
				
				
				Timestamp timestamp = new Timestamp(lectionDateRaw + sunriseTimeRaw);
				String updateSql= "update LECTION set LECTION_DTM=? where ID=?";
				PreparedStatement updateDtmStatement =connection.prepareStatement(updateSql);
				updateDtmStatement.setTimestamp(1, timestamp);
				updateDtmStatement.setLong(2, lectionId);
				updateDtmStatement.executeUpdate();
			}	
			lectionResultSet.close();

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
