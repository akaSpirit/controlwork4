import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cat implements Feedable, Playable, Curable {
    private String name;
    private int age; //1..18
    private int satietyLevel; //0..100
    private int moodLevel; //0..100
    private int healthLevel; //0..100
    private static final Random r = new Random();
    private static final List<String> names = List.of("Peach", "Ginger", "Toby", "Seth", "Tibbles", "Tabby", "Poppy", "Millie", "Daisy", "Jasper", "Misty", "Minka");
    private int average;
    private transient boolean isActionToday;

    public Cat() {
        name = names.get(r.nextInt(names.size()));
        this.age = r.nextInt(1, 19);
        this.satietyLevel = r.nextInt(20, 81);
        this.moodLevel = r.nextInt(20, 81);
        this.healthLevel = r.nextInt(20, 81);
        refreshAverage();
        this.isActionToday = false;
    }

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static List<Cat> makeCats(int amount) {
        return Stream.generate(Cat::new)
                .distinct()
                .limit(amount)
                .collect(Collectors.toList());
    }

    public void setSatietyLevel(int satietyLevel) {
        if (satietyLevel < 0) this.satietyLevel = 0;
        else this.satietyLevel = Math.min(satietyLevel, 100);
    }

    public void setMoodLevel(int moodLevel) {
        if (moodLevel < 0) this.moodLevel = 0;
        else this.moodLevel = Math.min(moodLevel, 100);
    }

    public void setHealthLevel(int healthLevel) {
        if (healthLevel < 0) this.healthLevel = 0;
        else this.healthLevel = Math.min(healthLevel, 100);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSatietyLevel() {
        return satietyLevel;
    }

    public int getMoodLevel() {
        return moodLevel;
    }

    public int getHealthLevel() {
        return healthLevel;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public void refreshAverage() {
        average = (satietyLevel + moodLevel + healthLevel) / 3;
    }

    public boolean isActionToday() {
        return isActionToday;
    }

    public void setActionToday(boolean actionToday) {
        isActionToday = actionToday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat cat = (Cat) o;
        return Objects.equals(name, cat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public void feed() {
        if (!isActionToday()) {
            System.out.printf("%nYou fed the cat %s, %s years old%n%n", getName(), getAge());
            if (getAge() < 6) {
                satietyLevel += 7;
                moodLevel += 7;
            } else if (getAge() >= 6 && getAge() <= 10) {
                satietyLevel += 5;
                moodLevel += 5;
            } else if (getAge() > 10) {
                satietyLevel += 4;
                moodLevel += 4;
            }
            setActionToday(true);
            refreshAverage();
            name = name + " *";
        } else System.out.println("\nCan't perform an action with this cat today.\n");
    }

    @Override
    public void cure() {
        if (!isActionToday()) {
            System.out.printf("%nYou cured the cat %s, %s years old%n%n", getName(), getAge());
            if (getAge() < 6) {
                healthLevel += 7;
                moodLevel -= 3;
                satietyLevel -= 3;
            } else if (getAge() >= 6 && getAge() <= 10) {
                healthLevel += 5;
                moodLevel -= 5;
                satietyLevel -= 5;
            } else if (getAge() > 10) {
                healthLevel += 4;
                moodLevel -= 6;
                satietyLevel -= 6;
            }
            setActionToday(true);
            refreshAverage();
            name = name + " *";
        } else System.out.println("\nCan't perform an action with this cat today.\n");
    }

    @Override
    public void play() {
        if (!isActionToday()) {
            System.out.printf("%nYou played with the cat %s, %s years old%n%n", getName(), getAge());
            if (getAge() < 6) {
                moodLevel += 7;
                healthLevel += 7;
                satietyLevel -= 3;
            } else if (getAge() >= 6 && getAge() <= 10) {
                moodLevel += 5;
                healthLevel += 5;
                satietyLevel -= 5;
            } else if (getAge() > 10) {
                moodLevel += 5;
                healthLevel += 4;
                satietyLevel -= 6;
            }
            setActionToday(true);
            refreshAverage();
            name = name + " *";
        } else System.out.println("\nCan't perform an action with this cat today.\n");
    }

    public void changeParams() {
        satietyLevel -= r.nextInt(1, 6);
        moodLevel += r.nextInt(-3, 4);
        healthLevel += r.nextInt(-3, 4);
        refreshAverage();
    }

    public void resetActionToday() {
        isActionToday = false;
        name = name.replace(" *", "");
    }
}
