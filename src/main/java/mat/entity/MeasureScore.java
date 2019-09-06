package mat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEASURE_SCORE")
public class MeasureScore {
	
	public static class Comparator implements java.util.Comparator<MeasureScore> {

		@Override
		public int compare(MeasureScore o1, MeasureScore o2) {
			return o1.getScore().compareTo(o2.getScore());
		}
		
	}
	
	private String id;
	
	private String score;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "SCORE", nullable = false, length = 200)
	public String getScore() {
		return score;
	}
	
	public void setScore(String score) {
		this.score = score;
	}
	
	

}
