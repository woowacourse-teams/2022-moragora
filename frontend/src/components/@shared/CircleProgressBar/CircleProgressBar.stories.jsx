import CircleProgressBar from '.';

export default {
  title: 'Components/CircleProgressBar',
  component: CircleProgressBar,
};

const Template = (args) => {
  return <CircleProgressBar {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  size: 250,
  percent: 20,
};
