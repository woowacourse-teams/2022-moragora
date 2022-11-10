// minute이 60을 넘어갈 때 hour + 1
// 24시를 넘어갈 때 0으로 수정
const reconcileTime = (time: string) => {
  const timeList = time.split(':');
  let hour = parseInt(timeList[0], 10);
  let minute = parseInt(timeList[1], 10);

  if (minute >= 60) {
    hour += 1;
    minute -= 60;
  }

  return `${`0${hour % 24}`.slice(-2)}:${`0${minute}`.slice(-2)}`;
};

// 분에 minute에 해당하는만큼 추가
export const addMinute = (startTime: string, minute: number) => {
  const timeList = startTime.split(':');
  const startHour = timeList[0];
  const startMinute = parseInt(timeList[1], 10);

  const addedTime = `${startHour}:${`0${startMinute + minute}`.slice(-2)}`;

  return reconcileTime(addedTime);
};

export const dateToFormattedString = (date: Date) => {
  const year = date.getFullYear().toString();
  const month = (1 + date.getMonth()).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');

  return `${year}-${month}-${day}`;
};

export const getAllDatesInMonth = (year: number, month: number) => {
  const date = new Date(year, month, 1);

  const dates = [];

  while (date.getMonth() === month) {
    dates.push(new Date(date));
    date.setDate(date.getDate() + 1);
  }

  return dates;
};

export const getlastDateOfMonth = (date: Date) =>
  new Date(date.getFullYear(), date.getMonth() + 1, 0);

export const getAllSameDays = (day: number, begin: Date, end: Date) => {
  const dates: Date[] = [];
  const beginDate = new Date(begin);
  const endDate = new Date(
    end.getFullYear(),
    end.getMonth(),
    end.getDate() + 1
  );

  endDate.setMilliseconds(-1);
  beginDate.setDate(beginDate.getDate() + 1);

  if (day > 6) {
    return dates;
  }

  while (beginDate.getDay() !== day) {
    beginDate.setDate(beginDate.getDate() + 1);
  }

  while (beginDate.getTime() <= endDate.getTime()) {
    dates.push(new Date(beginDate));
    beginDate.setDate(beginDate.getDate() + 7);
  }

  return dates;
};

export const getTimestampDiff = (
  aheadTimestamp: number,
  behindTimestamp: number
) => {
  if (aheadTimestamp <= behindTimestamp) {
    return '00:00';
  }

  const minute = Math.floor((aheadTimestamp - behindTimestamp) / 1000 / 60);
  const second = Math.floor(((aheadTimestamp - behindTimestamp) / 1000) % 60);
  const leftPaddedMinute = minute.toString().padStart(2, '0');
  const leftPaddedSecond = second.toString().padStart(2, '0');

  return `${leftPaddedMinute}:${leftPaddedSecond}`;
};
