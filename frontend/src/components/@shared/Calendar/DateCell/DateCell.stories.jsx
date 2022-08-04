import DateCell from '.';

export default {
  title: 'Components/DateCell',
  component: DateCell,
};

const Template = (args) => {
  return <DateCell {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  date: new Date(),
};

export const Disabled = Template.bind({});
Disabled.args = {
  date: new Date(),
  disabled: true,
};
