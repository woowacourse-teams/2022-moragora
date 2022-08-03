import CoffeeStackModal from '.';

export default {
  title: 'Components/CoffeeStackModal',
  component: CoffeeStackModal,
};

const Template = (args) => {
  return <CoffeeStackModal {...args} />;
};

export const Default = Template.bind({});
Default.args = {};
