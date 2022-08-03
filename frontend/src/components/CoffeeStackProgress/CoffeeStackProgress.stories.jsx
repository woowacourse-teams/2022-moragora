import CoffeeStackProgress from '.';

export default {
  title: 'Components/CoffeeStackProgress',
  component: CoffeeStackProgress,
};

const Template = (args) => {
  return <CoffeeStackProgress {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  percent: 20,
};
