import { CalendarProvider } from 'contexts/calendarContext';
import Calendar from '.';

export default {
  title: 'Components/Calendar',
  component: Calendar,
};

const Template = (args) => {
  return (
    <CalendarProvider initialDate={args.date}>
      <Calendar />
    </CalendarProvider>
  );
};

export const Default = Template.bind({});
Default.args = {
  date: new Date(),
};
