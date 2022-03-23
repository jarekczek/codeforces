from datetime import date
import dateutil.parser
import matplotlib.pyplot as plt
import pandas as pd

df = pd.read_csv('colors_2010_2022.txt', delimiter="\t")
print(df.head())
#df = df.set_index('reportDate')
dates = sorted(df['reportDate'].unique())
print(dates)

df2 = pd.DataFrame(index=dates)
df2['total'] = df[df['color'] == 'total'].set_index('reportDate')['count']
print(df2.head())

df3 = df[df['color'] == 'total']
#print(df3.head())
colors = sorted(df['color'].unique())
for color in colors:
  print(color)
  df2[color] = df[df['color'] == color].set_index('reportDate')['count']
  df2[color + '_rel'] = 100.0 * df2[color] / df2['total']

df2[colors[0] + '_cumul'] = df2[colors[0] + '_rel']
for i in range(1, len(colors)):
  print(colors[i])
  df2[colors[i] + '_cumul'] = df2[colors[i] + '_rel'] + df2[colors[i-1] + '_cumul']
  
def dateToNumber(strDate):
  d = dateutil.parser.parse(strDate)
  dJan = date(d.year, 1, 1)
  return d.year + (d.toordinal() - dJan.toordinal()) / 365.0
  
df2['dateNumber'] = df2.apply(lambda row: dateToNumber(row.name), axis=1)
print(df2.head())
  
plt.clf()
plt.plot(df2['dateNumber'], df2['total'])
plt.plot(df2['dateNumber'], df2['0000_gray'], color='#808080')
plt.plot(df2['dateNumber'], df2['1200_green'], color='#008000')
plt.plot(df2['dateNumber'], df2['1400_cyan'], color='#03a89e')
plt.plot(df2['dateNumber'], df2['1600_blue'], color='#0000c0')
plt.plot(df2['dateNumber'], df2['1900_violet'], color='#a000a0')
plt.plot(df2['dateNumber'], df2['2100_orange'], color='#ff8c00')
plt.plot(df2['dateNumber'], df2['2600_red'], color='#ff0000')
plt.savefig('total.png')

plt.clf()
plt.plot(df2['dateNumber'], df2['0000_gray_rel'], color='#808080')
plt.plot(df2['dateNumber'], df2['1200_green_rel'], color='#008000')
plt.plot(df2['dateNumber'], df2['1400_cyan_rel'], color='#03a89e')
plt.plot(df2['dateNumber'], df2['1600_blue_rel'], color='#0000c0')
plt.plot(df2['dateNumber'], df2['1900_violet_rel'], color='#a000a0')
plt.plot(df2['dateNumber'], df2['2100_orange_rel'], color='#ff8c00')
plt.plot(df2['dateNumber'], df2['2600_red_rel'], color='#ff0000')
plt.savefig('total_rel.png')

plt.clf()
plt.plot(df2['dateNumber'], df2['0000_gray_cumul'], color='#808080')
plt.plot(df2['dateNumber'], df2['1200_green_cumul'], color='#008000')
plt.plot(df2['dateNumber'], df2['1400_cyan_cumul'], color='#03a89e')
plt.plot(df2['dateNumber'], df2['1600_blue_cumul'], color='#0000c0')
plt.plot(df2['dateNumber'], df2['1900_violet_cumul'], color='#a000a0')
plt.plot(df2['dateNumber'], df2['2100_orange_cumul'], color='#ff8c00')
plt.plot(df2['dateNumber'], df2['2600_red_cumul'], color='#ff0000')
plt.savefig('total_rel_cumul.png')

print("ok")
