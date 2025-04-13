-- Drop the table if it exists
DROP TABLE IF EXISTS exercise;

-- Create the exercise table
CREATE TABLE exercise (
    id SERIAL PRIMARY KEY,
    exercise_name VARCHAR(255) NOT NULL,
    muscle_group VARCHAR(255) NOT NULL
);

-- Insert exercise data
INSERT INTO exercise (exercise_name, muscle_group) VALUES
-- Chest
('Bench Press', 'Chest'),
('Incline Dumbbell Press', 'Chest'),
('Chest Fly', 'Chest'),
('Push-Up', 'Chest'),
('Cable Crossover', 'Chest'),
('Decline Bench Press', 'Chest'),
-- Back
('Pull-Up', 'Back'),
('Barbell Row', 'Back'),
('Deadlift', 'Back'),
('Lat Pulldown', 'Back'),
('T-Bar Row', 'Back'),
('Seated Cable Row', 'Back'),
-- Legs
('Squat', 'Legs'),
('Leg Press', 'Legs'),
('Lunges', 'Legs'),
('Romanian Deadlift', 'Legs'),
('Leg Curl', 'Legs'),
('Leg Extension', 'Legs'),
-- Biceps
('Barbell Curl', 'Biceps'),
('Hammer Curl', 'Biceps'),
('Concentration Curl', 'Biceps'),
('Incline Dumbbell Curl', 'Biceps'),
('Cable Curl', 'Biceps'),
-- Triceps
('Tricep Pushdown', 'Triceps'),
('Overhead Tricep Extension', 'Triceps'),
('Close-Grip Bench Press', 'Triceps'),
('Dips', 'Triceps'),
('Kickbacks', 'Triceps'),
-- Abs
('Crunches', 'Abs'),
('Plank', 'Abs'),
('Leg Raise', 'Abs'),
('Bicycle Crunch', 'Abs'),
('Hanging Knee Raise', 'Abs'),
-- Shoulders
('Overhead Press', 'Shoulders'),
('Lateral Raise', 'Shoulders'),
('Front Raise', 'Shoulders'),
('Arnold Press', 'Shoulders'),
('Face Pull', 'Shoulders');
