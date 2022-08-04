import WeekCell from '.';

export default {
  title: 'Components/WeekCell',
  component: WeekCell,
};

const Template = (args) => {
  return <WeekCell {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  children: '일',
};
