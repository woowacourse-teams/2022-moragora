import CoffeeStackItemSkeleton from '.';

export default {
  title: 'Components/CoffeeStackItemSkeleton',
  component: CoffeeStackItemSkeleton,
};

const Template = (args) => {
  return <CoffeeStackItemSkeleton {...args} />;
};

export const Default = Template.bind({});
