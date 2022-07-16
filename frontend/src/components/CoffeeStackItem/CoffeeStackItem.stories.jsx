import CoffeeStackItem from '.';

export default {
  title: 'Components/CoffeeStackItem',
  component: CoffeeStackItem,
};

const Template = (args) => {
  return <CoffeeStackItem {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  meeting: {
    id: 1,
    name: '모임1',
    attendanceCount: 0,
    startDate: '2022-01-01',
    endDate: '2022-12-31',
    entranceTime: '10:00',
    leaveTime: '18:00',
  },
};
