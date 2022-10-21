import { useEffect, useRef } from 'react';
import * as S from './TimePicker.styled';

type TimePickerProps = {
  name: string;
  min: { hour: number; minute: number };
  max: { hour: number; minute: number };
  disabled: boolean;
  value: Partial<{ hour: number; minute: number }>;
  onChange: (value: Partial<{ hour: number; minute: number }>) => void;
};

const TimePicker = ({
  name,
  min,
  max,
  value,
  disabled,
  onChange,
}: Partial<TimePickerProps>) => {
  const hourSelectRef = useRef<HTMLSelectElement>(null);
  const minuteSelectRef = useRef<HTMLSelectElement>(null);
  const minutes = [0, 10, 20, 30, 40, 50]
    .filter(
      (minute) =>
        !min ||
        Number(hourSelectRef.current?.value) >= min.hour ||
        minute > min.minute
    )
    .filter(
      (minute) =>
        !max ||
        Number(hourSelectRef.current?.value) < max.hour ||
        minute <= max.minute
    );

  const hours = [
    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    21, 22, 23,
  ]
    .filter((hour) => !min || min?.minute < 50 || hour > min.hour)
    .filter((hour) => !min || hour >= min.hour)
    .filter((hour) => !max || hour <= max.hour) as number[];

  useEffect(() => {
    onChange?.({ hour: hours[0], minute: minutes[0] });
  }, []);

  return (
    <S.Layout>
      <select
        name={`${name}-hour`}
        ref={hourSelectRef}
        value={value?.hour}
        onChange={(e) => {
          onChange?.({
            hour: Number(e.target.value),
          });
        }}
        disabled={disabled}
      >
        {hours.map((hour) => (
          <option key={hour} value={hour}>
            {hour.toString().padStart(2, '0')}
          </option>
        ))}
      </select>
      <span>:</span>
      <select
        name={`${name}-minute`}
        ref={minuteSelectRef}
        value={value?.minute}
        onChange={(e) => {
          onChange?.({
            minute: Number(e.target.value),
          });
        }}
        disabled={disabled}
      >
        {minutes.map((minute) => (
          <option key={minute} value={minute}>
            {minute.toString().padStart(2, '0')}
          </option>
        ))}
      </select>
    </S.Layout>
  );
};

export default TimePicker;
