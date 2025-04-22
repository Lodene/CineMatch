export type TimeAgoUnit = 'minutes' | 'hours' | 'days' | 'weeks' | 'months' | 'years';

export function parseLocalDateTimeWithOffset(localDateTime: string, timezoneOffsetHours: number): Date {
  const [datePart, timePart] = localDateTime.trim().split('T');
  const [year, month, day] = datePart.split('-').map(Number);

  const [hour, minute, secondsAndMillis = '0'] = timePart.split(':');
  const [second, millis = '0'] = secondsAndMillis.split('.');

  // Create as if it's local, then adjust manually for the fixed offset
  const date = new Date(
    year,
    month - 1,
    day,
    Number(hour),
    Number(minute),
    Number(second),
    Number(millis.slice(0, 3))
  );

  // Add timezone offset to correct for UTC interpretation
  date.setHours(date.getHours() + timezoneOffsetHours);
  return date;
}

export function timeAgoValue(date: string | Date): { value: number, unit: TimeAgoUnit } {
  const now = new Date().getTime();
  const inputTime = typeof date === 'string'
    ? parseLocalDateTimeWithOffset(date, 2) // ⬅️ Hardcoded +2h offset from server
    .getTime()
    : date.getTime();

  const diffInMs = now - inputTime;
  const diffInHours = diffInMs / (1000 * 60 * 60);

  if (diffInHours < 1) {
    const minutes = Math.floor(diffInMs / (1000 * 60));
    return { value: minutes, unit: 'minutes' };
  } else if (diffInHours < 24) {
    return { value: Math.floor(diffInHours), unit: 'hours' };
  } else if (diffInHours < 168) {
    return { value: Math.floor(diffInHours / 24), unit: 'days' };
  } else if (diffInHours < 672) {
    return { value: Math.floor(diffInHours / 168), unit: 'weeks' };
  } else if (diffInHours < 8760) {
    return { value: Math.floor(diffInHours / 672), unit: 'months' };
  } else {
    return { value: Math.floor(diffInHours / 8760), unit: 'years' };
  }
}
