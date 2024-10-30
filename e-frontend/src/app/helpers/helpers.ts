export const parseTime = (time: string): number => {
  return parseInt(time.split(':')[0], 10);
};

export const generateTimeSlots = (
  openingHour: number,
  closingHour: number
): string[] => {
  const slots: string[] = [];
  for (let hour = openingHour; hour < closingHour; hour++) {
    slots.push(formatHour(hour));
  }
  return slots;
};

export const formatHour = (hour: number): string => {
  const suffix = hour >= 12 ? 'PM' : 'AM';
  const formattedHour = hour > 12 ? hour - 12 : hour === 0 ? 12 : hour;
  return `${formattedHour}:00 ${suffix}`;
};

export const formatDate = (date: Date): string => {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');

  return `${year}-${month}-${day}`;
};

export const convertTo24HourFormat = (time: string): string => {
  const [timePart, modifier] = time.split(' ');
  let [hours, minutes] = timePart.split(':').map(Number);

  if (modifier === 'PM' && hours < 12) {
    hours += 12;
  }
  if (modifier === 'AM' && hours === 12) {
    hours = 0;
  }

  const formattedHours = hours.toString().padStart(2, '0');
  const formattedMinutes = minutes.toString().padStart(2, '0');

  return `${formattedHours}:${formattedMinutes}:00`;
};
