package modules.independent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import conditions.channel.IDChannelCondition;
import conditions.status.FileExists;
import conditions.status.RunTimesPerWeek;
import conditions.status.WeekDay;
import sx.blah.discord.util.EmbedBuilder;

public class LunchAssignementNotifier extends Independent {
	private static final String ONLINE_SHEET = "";
	private static final String CSV_NAME = "Assignments.csv";
	private static final String CHANNEL_ID = "";

	public LunchAssignementNotifier() {
		this( Status.ENABLED );
	}

	public LunchAssignementNotifier( Status status ) {
		log.log("Initializing Lunch Assignment displayer...");
		this.status = status;
		this.name = "iLunchManager";
		this.help = "**__iLunchManager__**"  +
				"\nThis utility is used to display assignments for the lunch rotation system."  +
				"\nIt has no command to manually invoke, and instead is automatically called."  +
				"\nThere are no subcommands available.";

		/* Conditions required:
		 * Only run on one day of the week
		 * Run once a week
		 * File must exist on local system
		 */
		statusConditions.add( new WeekDay( DayOfWeek.SUNDAY ) );
		statusConditions.add( new RunTimesPerWeek( 1, DayOfWeek.SATURDAY ) );
		statusConditions.add( new FileExists( CSV_NAME ) );

		channelConditions.add( new IDChannelCondition( CHANNEL_ID ) );
	}

	@Override
	protected void run() {
		List<CSVDay> days = readCSV( LocalDate.now(), LocalDate.now().plusWeeks(1) );

		for( CSVDay d : days ) {
			EmbedBuilder embed1 = new EmbedBuilder();

			embed1.withTitle("Assignments for the coming week of lunch rotations");
			embed1.withDescription(d.getDayOfWeekEnum().toString() + " (" + d.date + ")");
			embed1.withUrl(ONLINE_SHEET);
			embed1.withColor(255, 0, 185);
			embed1.withAuthorName("Lunch Rotations");
			embed1.withAuthorIcon("https://png.icons8.com/metro/52/000000/to-do.png");
			embed1.appendField("Person 1", d.personA, true); // Somehow implement @ people
			embed1.appendField("Person 2", d.personB, true);
			embed1.appendField("Person 3", d.personC, true);
			embed1.appendField("Backup 1", d.backupA, true);
			embed1.appendField("Backup 2", d.backupB, true);
			embed1.withFooterText("Generated automagically at: " + LocalTime.now().toString());
			embed1.withFooterIcon("https://cdn.discordapp.com/attachments/360656219104477186/411679252283523083/caris_2.png");

			embed.add( embed1 );
		}
	}

	private List<CSVDay> readCSV( LocalDate start, LocalDate end ) {
		List<CSVDay> validDays = new ArrayList<>();
		AssignmentCSVReader reader = new AssignmentCSVReader( CSV_NAME );
		CSVDay[] days = reader.getAllDayData();
		
		for( CSVDay d : days ) {
			if( ( d.getDate().isBefore(end) || d.getDate().isEqual(end) ) && ( d.getDate().isAfter(start) || d.getDate().isEqual(start) ) ) {
				validDays.add(d);
			}
		}
		
		return validDays;
	}
	
	/* COPIED FROM LUNCHASSIGNERDRONE DON'T WORRY IT'LL BE FINE I PROMISE */
	
	public class CSVDay {
		
		@CsvBindByName(column = "Date", required = true)
		protected String date;
		
		@CsvBindByName(column = "Day Of Week", required = false)
		protected String dayOfWeek;

		@CsvBindByName(column = "Person A", required = false)
		protected String personA;

		@CsvBindByName(column = "Person B", required = false)
		protected String personB;

		@CsvBindByName(column = "Person C", required = false)
		protected String personC;

		@CsvBindByName(column = "Backup A", required = false)
		protected String backupA;

		@CsvBindByName(column = "Backup B", required = false)
		protected String backupB;

		@CsvBindByName(column = "Status", required = false)
		protected String status;
		
		public DayOfWeek getDayOfWeekEnum() {
			return DayOfWeek.valueOf(dayOfWeek);
		}
		
		LocalDate getDate() {
			return LocalDate.parse(date);
		}

	}
	
	public class AssignmentCSVReader {
		private String fileName;

		private List<CSVDay> csvDays;

		public AssignmentCSVReader( String fileName ) {
			this.fileName = fileName;

			this.csvDays = new ArrayList<>();
		}

		public CSVDay[] getAllDayData() {
			if( csvDays.size() == 0 ) { // Only calculate this once.
				loadDayData();
			}

			return csvDays.toArray( new CSVDay[ csvDays.size() ] );
		}
		
		public void loadDayData() {
			try( BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( new File( fileName ) ) ) ); ) {
				CsvToBean<CSVDay> csvToBean = new CsvToBeanBuilder(reader).withType(CSVDay.class).withIgnoreLeadingWhiteSpace(true).build();

				csvDays = csvToBean.parse();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}

