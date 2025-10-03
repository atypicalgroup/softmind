package br.com.atypical.Softmind.Mood.entities;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "gs_tb_daily_moods")
public class DailyMood {

    @Id
    private String id;

    private String employeeId;
    private String companyId;
    private String emoji;
    private String feeling;
    private LocalDateTime createdAt = LocalDateTime.now();

    public DailyMood() {
    }

    public DailyMood(String id, String employeeId, String companyId, String emoji, String feeling, LocalDateTime createdAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.companyId = companyId;
        this.emoji = emoji;
        this.feeling = feeling;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
