import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DnsRecord {
    private String name;
    private String value;
    private short type;
    private short ttl;

    public DnsRecord(String name, String value, short type, short ttl) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.ttl = ttl;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public short getType() {
        return type;
    }

    public short getTtl() {
        return ttl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(short type) {
        this.type = type;
    }

    public void setTtl(short ttl) {
        this.ttl = ttl;
    }

    public static void writeRecordsToFile(DnsRecord[] records, String fileName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        for (DnsRecord record : records) {
            bw.write(record.getName() + " " + record.getValue() + " " + record.getType() + " " + record.getTtl());
            bw.newLine();
        }
        bw.close();
    }

    public static DnsRecord[] readRecordsFromFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        int count = 0;
        while ((line = br.readLine()) != null) {
            count++;
        }
        br.close();

        br = new BufferedReader(new FileReader(fileName));
        DnsRecord[] records = new DnsRecord[count];
        count = 0;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            String name = parts[0];
            String value = parts[1];
            short type = Short.parseShort(parts[2]);
            short ttl = Short.parseShort(parts[3]);
            records[count] = new DnsRecord(name, value, type, ttl);
            count++;
        }
        br.close();
        return records;
    }
}
